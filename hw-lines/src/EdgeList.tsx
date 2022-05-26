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

import React, { Component } from 'react';
import { Edge } from './GeoConstructs';

interface EdgeListProps {
    onChange(edges: Edge[]): void;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, {tBoxText: string}> {

    constructor(props: any)
    {
        super(props);
        this.state = { tBoxText: "" };
    }

    render() 
    {
        return (
            <div id="edge-list">
                Edges <br/>

                <textarea
                    rows={5}
                    cols={30}
                    onChange={(e) => {
                        this.setState({
                            tBoxText: e.target.value
                        })
                    }}
                    value={this.state.tBoxText}
                /> 

                <br/>

                <button onClick={() => 
                    {
                        this.onDrawClicked();
                    }}>
                        Draw Edges
                        </button>

                <button onClick={
                    () => 
                    {
                        this.setState({ tBoxText: "" })
                        this.props.onChange([]);
                    }
                }
                >
                    Clear Edges
                </button>
            </div>
        );
    }

    spawnErrorBox(reason: string, offendingLine: string, lineNum: number)
    {
        alert(reason + "\n" + 
              "Error on line " + (lineNum)  + ": \"" + offendingLine + "\"");
    }

    getValidatedEdge(e:any, x1r:any, y1r:any, x2r:any, y2r:any, color:any, index:any) : Edge | null
    {
        let lx1:number = +x1r;
        let ly1:number = +y1r;
        let lx2:number = +x2r;
        let ly2:number = +y2r;

        // validate line types.
        if (Number.isNaN(lx1) || 
            Number.isNaN(ly1) || 
            Number.isNaN(lx2) || 
            Number.isNaN(ly2))
        {
            this.spawnErrorBox("x1, y1, x2, y2 must be numbers", e, index + 1);
            return null;
        }

        // validate line values.
        if (lx1 < 0 || lx1 > 4000)
        {
            this.spawnErrorBox("x1 must be in range [0,4000]", e, index + 1);
            return null;
        }

        if (ly1 < 0 || ly1 > 4000)
        {
            this.spawnErrorBox("y1 must be in range [0,4000]", e, index + 1);
            return null;
        }

        if (lx2 < 0 || lx2 > 4000)
        {
            this.spawnErrorBox("x2 must be in range [0,4000]", e, index + 1);
            return null;
        }

        if (ly2 < 0 || ly2 > 4000)
        {
            this.spawnErrorBox("y2 must be in range [0,4000]", e, index + 1);
            return null;
        }

        let resEdge:Edge = 
        { 
            id: index,
            x1: lx1, 
            y1: ly1, 
            x2: lx2, 
            y2: ly2,
            color: color
        };
        
        return resEdge;
    }

    onDrawClicked()
    {
        let tBoxText = this.state.tBoxText.trim();
        console.log("Draw Clicked!\nEdgeList raw text:\n\"%s\"", tBoxText)

        // split text in textBox by lines
        let lines = tBoxText.split(/\r\n|\r|\n/);
        let edges:Edge[] = []

        let errFound:boolean = false;
        let index:number = 0;

        lines.every(e => 
        {
            let trimmedLn = e.trim();
            let splitParts = trimmedLn.split(" ");

            // validate line length.
            if (splitParts.length !== 5)
            {
                this.spawnErrorBox("Each line must be formatted as follows: <x1> <y1> <x2> <y2> <color>.", 
                                    e, index + 1);
                errFound = true;
                return false;
            }

            // validate line fields.
            let edge = this.getValidatedEdge(e, splitParts[0], splitParts[1], splitParts[2], 
                splitParts[3], splitParts[4], index);

            if (edge == null)
            {
                errFound = true;
                return false;
            }

            edges.push(edge);
            index++;
            return true;
        });

        if (errFound)
        {
            console.log("Invalid input found");
        }
        else
        {
            console.log("Input validated, dispatching EdgeListProps onChange!");
            this.props.onChange(edges);
        }
    }
}

export default EdgeList;
