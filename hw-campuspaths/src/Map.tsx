/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import L, { LatLngExpression, LatLngLiteral } from "leaflet";
import { Component } from "react";
import { MapContainer, Marker, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE, UW_LATITUDE_CENTER, UW_LATITUDE_OFFSET, UW_LATITUDE_SCALE, UW_LONGITUDE, UW_LONGITUDE_CENTER, UW_LONGITUDE_OFFSET, UW_LONGITUDE_SCALE } from "./Constants";
import { Edge, Point } from "./GeoConstructs";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// NOTE: This component is a suggestion for you to use, if you would like to. If
// you don't want to use this component, you're free to delete it or replace it
// with your hw-lines Map

interface MapProps 
{
  routeEdges: Edge[],
  startPoint: Point|null,
  endPoint: Point|null
}

// custom icon for start flag w/ offset correction added
const startIcon = L.icon({
  iconUrl: 'start_marker.svg',
  iconSize: [32,32],
  iconAnchor: [16,32]
});

// custom icon for end flag w/ offset correction added
const finishIcon = L.icon({
  iconUrl: 'finish_flag.png',
  iconSize: [32,32],
  iconAnchor: [3.5,31]
});


class Map extends Component<MapProps, {}> 
{
  // converts a 2D point (x,y) in UW Map coordinates to Earth coordinates (latitude and longitude).
  pointToLatLng(point: Point) : LatLngLiteral
  {
    return { lat: UW_LATITUDE + (point.y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE, 
             lng: UW_LONGITUDE + (point.x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE };
  }

  render() 
  {
    let edges = this.props.routeEdges;

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          {
              // draw the edges of the campus route on the map.
              edges.map((edge) => 
              {
                return <MapLine key={edge.id} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2}/>
              }) 
          }
          
          {
            // if a starting point exists, draw it on the map with the appropriate icon. 
            this.props.startPoint == null ? [] : 
              <Marker position={this.pointToLatLng(this.props.startPoint)} icon={startIcon} />
          }

          {
            // if an end point exists, draw it on the map with the appropriate icon. 
            this.props.endPoint == null ? [] : 
              <Marker position={this.pointToLatLng(this.props.endPoint)} icon={finishIcon} />
          }

        </MapContainer>
      </div>
    );
  }
}

export default Map;
