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

import { LatLngExpression, map } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import EdgeList, { Edge } from "./EdgeList";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  myEdges:Edge[]
}

interface MapState {
}

class Map extends Component<MapProps, MapState> {
  constructor(props:any)
  {
    super(props);
  }

  render() {
    console.log("Map render called");
    let edges = this.props.myEdges;
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
            edges.map(function (e) 
            {
              return <MapLine key={e.id} color={e.color} x1={e.x1} y1={e.y1} x2={e.x2} y2={e.y2}/>
            })
          }
        </MapContainer>
        <button onClick={() => 
            {
              console.log("Save clicked");
              
            }}>
          Save Image
          </button>
      </div>
    );
  }
}

export default Map;
