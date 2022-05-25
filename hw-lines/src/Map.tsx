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

import L, { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, Marker, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE, UW_LATITUDE_CENTER, UW_LATITUDE_OFFSET, UW_LATITUDE_SCALE, UW_LONGITUDE, UW_LONGITUDE_CENTER, UW_LONGITUDE_OFFSET, UW_LONGITUDE_SCALE } from "./Constants";
import { Edge } from "./EdgeList";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  myEdges: Edge[]
  myPoints: Point[]

  onAddPointClicked(points: Point) : void;
  onUndoPointClicked() : void;
  onClearAllPointsClicked() : void;
  onMeasureDistClicked() : void;
}

interface MapState {
  myPoints: Point[],
  pointAddX: string,
  pointAddY: string
}

export interface Point
{
  x:number,
  y:number
}

// sound effects for point operations
const meow = new Audio("meow.mp3");
const bruh = new Audio("bruh.mp3");
const pipe = new Audio("pipe.mp3");

const pointIcon = L.icon({
  iconUrl: 'marker_blue.png',
  iconSize: [32,32],
  iconAnchor: [15,32]
});

class Map extends Component<MapProps, MapState> 
{
  constructor(props: any)
  {
    super(props);
    this.state = { myPoints: [], pointAddX : "", pointAddY : "" };
  }

  render() 
  {
    console.log("Map render called");
    let edges = this.props.myEdges;
    let points = this.props.myPoints;

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
            edges.map(function (edge) 
            {
              return <MapLine key={edge.id} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2}/>
            })
          }

          {
            points.map(function (point)
            {
              console.log("(" + point.x + "," + point.y + ")");
              return <Marker position={{ lat: UW_LATITUDE + (point.y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE, lng:
                UW_LONGITUDE + (point.x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE}}
                icon={pointIcon}
                eventHandlers={{
                  click: (e) => {
                    console.log('marker clicked', e)
                  },
                }}
              />;
            })
          }
        </MapContainer>

        <button onClick={() => 
            {
              console.log("Save clicked");
              window.print();
            }}>
          Save Image
        </button>
        
        &nbsp;
        &nbsp;
        [
        &nbsp;
        Point X:
        <textarea
            rows={1}
            cols={5} 
            style={{resize: "none"}}
            onChange={(e) => {
              this.setState({
                  pointAddX: e.target.value
              })
            }}
            value={this.state.pointAddX}/> 

        &nbsp;
        &nbsp;
        Point Y:
        <textarea
            rows={1}
            cols={5} 
            style={{resize: "none"}}
            onChange={(e) => {
              this.setState({
                pointAddY: e.target.value
              })
            }}
            value={this.state.pointAddY}/> 

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.addPoint();
            }}>
          Add Point
        </button>

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.props.onUndoPointClicked();
              bruh.play();
            }}>
          Undo Previous
        </button>

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.props.onClearAllPointsClicked();
              pipe.play();
            }}>
          Clear All Points
        </button>

        &nbsp;
        ]
        <br/>
        <button onClick={() => 
            {
              
            }}>
          Measure Distance
        </button>
        &nbsp;
        {this.state.myPoints}
      </div>
    );
  }

  addPoint()
  {
    let xText = this.state.pointAddX;
    let yText = this.state.pointAddY;
    let newX:number = +xText;
    let newY:number = +yText;

    if (isNaN(newX))
    {
      alert("Point X must be a number!");
      return;
    }

    if (isNaN(newY))
    {
      alert("Point Y must be a number!");
      return;
    }

    if (newX < 0 || newX > 4000)
    {
      alert("Point X must be in range [0, 4000]!");
      return;
    }

    if (newY < 0 || newY > 4000)
    {
      alert("Point Y must be in range [0, 4000]!");
      return;
    }

    this.props.onAddPointClicked({x:newX, y:newY});
    meow.play();
  }
}

export default Map;
