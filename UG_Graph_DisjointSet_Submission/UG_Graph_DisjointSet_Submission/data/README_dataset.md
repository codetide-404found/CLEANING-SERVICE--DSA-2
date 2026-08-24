# University of Ghana Campus Cleaning Graph Dataset

## Purpose
This dataset models the University of Ghana, Legon campus as a weighted, undirected graph for the DCIT 204/308 Ghana Smart Service Operations Optimizer.

- 55 campus locations (vertices)
- 111 direct connections (edges)
- Primary edge weight: estimated walking-route distance in metres
- `travelTime_min`: approximate walking time using 75 m/min
- `roadConditionWeight`: project modelling factor for route-condition/traffic effects
- `effectiveCost`: distance multiplied by the condition factor; optional for experiments

## Important data provenance note
The **locations and relative connectivity are map-informed**. The University of Ghana's official campus materials identify the Legon campus, its halls and major facilities. The University of Ghana campus map from the RSGIS Lab shows the named roads and facilities used to construct the network. A University campus guide and map-based nearby-distance listings were also used to sanity-check relative proximity.

The public map sources do **not** publish a surveyed distance for every one of the 111 selected connections. Therefore, the distance values in this project are explicitly **estimated/constructed walking-route distances**, not official surveyed measurements. They should not be described in the report as exact official UG distances.

## Coordinate note
`mapX` and `mapY` in `locations.csv` are **map-relative grid coordinates** for graph plotting/visualisation. They are not GPS coordinates and are not used as the primary edge weights.

## Suggested report wording
"Campus facilities and road topology were identified from University of Ghana campus materials and a campus map. Because an official surveyed distance dataset for every selected connection was not publicly available, edge distances were constructed as realistic walking-route estimates for algorithmic experimentation. The dataset was checked against the published campus layout and nearby-distance references. The same dataset is used consistently across graph algorithms and experiments."

## Required graph interpretation
- Vertex = campus facility/location
- Edge = directly usable campus connection/walkway/road
- Weight = estimated walking distance (m)
- Graph = undirected and weighted
- Adjacency list and adjacency matrix are both implemented
- BFS/DFS use connectivity
- Dijkstra uses `distance_m`
- Prim/Kruskal use `distance_m`
- `effectiveCost` can be used for a road-condition-aware experiment

## Source basis
1. University of Ghana — Welcome to UG Legon Campus.
2. University of Ghana — 2026 Matriculation Ceremony document, campus description.
3. University of Ghana RSGIS Lab — University of Ghana Campus Map.
4. Radio Univers — Finding your way around the streets of Legon.
5. Cartogiraffe — map-based nearby-distance information around Dr. J.B. Danquah Avenue.

