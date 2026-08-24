# Graph + Disjoint Set Role Package

This package is the graph/disjoint-set contribution for the University of Ghana campus cleaning-service DSA project.

Start with:
1. `report/Graph_Disjoint_Set_Role_Report.docx`
2. `data/README_dataset.md`
3. `data/locations.csv`
4. `data/roads.csv`
5. `src/GraphModule.java`
6. `src/GraphModuleTest.java`
7. `traces/`

Compile:
`javac src/GraphModule.java src/GraphModuleTest.java src/PerformanceRunner.java`

Test:
`java -cp src GraphModuleTest`

Run demo:
`java -cp src GraphModule data/roads.csv`

Run performance experiment:
`java -cp src PerformanceRunner`

The final performance CSV must be generated on the team's designated machine.
