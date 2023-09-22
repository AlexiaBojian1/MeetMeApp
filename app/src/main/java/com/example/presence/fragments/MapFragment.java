package com.example.presence.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.presence.objects.AppUser;
import com.example.presence.objects.Coords;
import com.example.presence.R;
import com.example.presence.objects.TextMessage;
import com.example.presence.activities.ModeratorViewMessageActivity;
import com.example.presence.activities.ViewOneMessageActivity;
import com.example.presence.exceptions.NullLocationException;
import com.example.presence.helpers.CustomMessagePasser;
import com.example.presence.helpers.LabelGetter;
import com.example.presence.helpers.LocationHelper;
import com.example.presence.helpers.MessageAdapter;
import com.example.presence.helpers.NicknameAdapter;
import com.example.presence.interfaces.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    ImageButton centerButton;
    private AppUser appUser;
    final private Integer viewRange = 50;
    LocationHelper locationHelper;
    GoogleMap map;

    @Override
    public void onPause() {
        // cancel any center location action that is currently scheduled
        locationHelper.stopLocationCallback();
        super.onPause();
    }

    @Override
    public void onResume() {
        if (map != null) {
            map.clear(); // clear map contents
            refreshMap(map); // update map contents
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        centerButton = getActivity().findViewById(R.id.centerButton);
        appUser = getArguments().getParcelable(LabelGetter.appUserLbl);

        View view=inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                // show map markers:
                refreshMap(map);
                // add listener that triggers view message activity. TODO: Is this correct/allowed?
                map.setOnMarkerClickListener(MapFragment.this);
                map.setOnInfoWindowClickListener(new OnInfoWindowClickListener());

                // go to the current location:
                focusOnUser(map, MapFragment.this);
                // register center button listener:
                centerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        focusOnUser(googleMap, MapFragment.this);
                    }
                });
                map.setInfoWindowAdapter(new CustomInfoViewAdapter());
            }
        });
        // Return view
        return view;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        // start viewMessageActivity for this message
        TextMessage message = (TextMessage) marker.getTag();
        Coords coords = new Coords(marker.getPosition().latitude, marker.getPosition().longitude);
        if (message.getDistanceToCoordinates(coords) > viewRange) {
            Toast.makeText(getActivity(), "Message is too far away to view",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }

        // return true to indicate that we wish to consume the event
        // ie no default behaviour (centering camera, opening infowindow) happens
        return true;
    }

    private class OnInfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            TextMessage message = (TextMessage) marker.getTag();
            Intent intent;
            if (message.getCreatorPhoneNumber().equals(appUser.getPhoneNumber())) { // if user posted this message themselves
                // go to activity without upvote, down vote and report
                intent = new Intent(getActivity(), ModeratorViewMessageActivity.class);
                intent.putExtra(LabelGetter.previousLbl, "HomeActivity");
            } else { // in case other user posted this message...
                // go to activity with upvote, down vote and report
                intent = new Intent(getActivity(), ViewOneMessageActivity.class);
            }
            intent = CustomMessagePasser.passMessage(intent, message);
            intent.putExtra(LabelGetter.appUserLbl, appUser);
            startActivity(intent);
        }
    }

    private void focusOnUser(GoogleMap map, Fragment mapFragment) {
        locationHelper = new LocationHelper(getActivity());

        try {
            locationHelper.startLocationCallback(new FocusOnUserHelper(map, mapFragment));
        } catch (NullLocationException e) {
            System.out.println(e);
        }
    }

    private void refreshMap(GoogleMap map) {
        LocationHelper locationHelper = new LocationHelper(getActivity());

        try {
            locationHelper.startLocationCallback(new MapRefresher(map));
        } catch (NullLocationException e) {
            System.out.println(e);
        }
    }

    private class MapRefresher implements LocationCallback {
        GoogleMap map;

        public MapRefresher(GoogleMap map) {
            this.map = map;
        }

        @Override
        public void onLocation(Location location) {
            // obtain coordinates via GPS
            Coords coords = new Coords(location.getLatitude(),
                    location.getLongitude());
            // database adapters
            MessageAdapter messageAdapter = new MessageAdapter(getActivity());
            NicknameAdapter nicknameAdapter = new NicknameAdapter(getActivity());
            messageAdapter.downloadMessagesInRange(coords, getContext(), 1000).
                    addOnCompleteListener(task ->{
                List<TextMessage> closeMessages = messageAdapter.getMessageList();
                // place buttons on the map for each message
                for (TextMessage message : closeMessages) {
                    LatLng messageLoc = new LatLng(
                            message.getCoordinates().getLatitude(),
                            message.getCoordinates().getLongitude());
                    // only show messages within 20 meters
                    if(message.getDistanceToCoordinates(coords)<=viewRange) {
                        nicknameAdapter.findNickname(message.getCreatorPhoneNumber())
                                .addOnCompleteListener(foundNickname -> {
                                    String nick = nicknameAdapter.getNickname();
                                    // if nickname is null for some reason, just show the last three
                                    // characters of the phone number
                                    if (nick == null) {
                                        nick = "..." + message.getCreatorPhoneNumber().substring(
                                                message.getCreatorPhoneNumber().length()
                                                        - 3);
                                    }
                                    Marker messageMarker = map.addMarker(new MarkerOptions()
                                            .position(messageLoc)
                                            .title(nick)
                                            // small hack to show the votes in the snippet
                                            .snippet(message.getVotes() + " " +
                                                    message.getText()));
                                    messageMarker.setTag(message);
                                    messageMarker.showInfoWindow();
                                });
                        // show only hints for messages within 1000 meters
                    } else if (message.getDistanceToCoordinates(coords)<=1000) {
                        Marker messageMarker = map.addMarker(new MarkerOptions()
                                .position(messageLoc));
                        messageMarker.setTag(message);
                        messageMarker.hideInfoWindow();
                    }
                }
            });
        }
    }

    private class FocusOnUserHelper implements LocationCallback {
        GoogleMap map;
        Fragment mapFragment;

        public FocusOnUserHelper(GoogleMap map, Fragment mapFragment) {
            this.map = map;
            this.mapFragment = mapFragment;
        }

        @Override
        public void onLocation(Location location) {
            map.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(location.getLatitude(),
                            location.getLongitude()),15));
            getActivity().getSupportFragmentManager()
                    .beginTransaction().show(mapFragment).commit();
            refreshMap(map);
        }
    }

    /**
     * Custom InfoWindow adapter for the GoogleMap
     */
    private class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

        private final View customWindowView;
        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView) customWindowView.findViewById(R.id.title));
            tvTitle.setText(marker.getTitle());
            String snippet = marker.getSnippet();
            String votes = snippet.substring(0, snippet.indexOf(" "));
            String text = snippet.substring(snippet.indexOf(" ")+1);
            votes = votes + " \uD83D\uDC9C";
            TextView tvSnippet = ((TextView) customWindowView.findViewById(R.id.snippet));
            tvSnippet.setText(text);
            TextView tvVotes = ((TextView) customWindowView.findViewById(R.id.votes));
            tvVotes.setText(votes);

            return customWindowView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        CustomInfoViewAdapter() {
            customWindowView = getLayoutInflater().inflate(R.layout.custom_map_marker, null);
        }
    }
}