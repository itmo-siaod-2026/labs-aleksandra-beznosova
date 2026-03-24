package com.lab


fun main() {
    val tree = KDTree()

    val points = listOf(
        KDTree.GeoUnit(1, "Point A", KDTree.Coords(55.75, 37.61)),
        KDTree.GeoUnit(2, "Point B", KDTree.Coords(54.72, 20.50)),
        KDTree.GeoUnit(3, "Point C", KDTree.Coords(59.93, 30.31)),
        KDTree.GeoUnit(4, "Point D", KDTree.Coords(56.84, 60.61)),
        KDTree.GeoUnit(5, "Point E", KDTree.Coords(43.12, 131.88)),
        KDTree.GeoUnit(6, "Point F", KDTree.Coords(55.03, 82.92)),
        KDTree.GeoUnit(7, "Point G", KDTree.Coords(47.23, 39.72)),
        KDTree.GeoUnit(8, "Point H", KDTree.Coords(51.53, 46.03))
    )

    for (point in points) {
        tree.insertUnit(point)

    }
    println(tree.tree)

}