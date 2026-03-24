package com.lab

class KDTree() {
    //Структура единицы объекта
    data class Coords(
        var lat: Double,
        var lng: Double
    )

    data class GeoUnit(
        val id: Int,
        val name: String,
        val cords: Coords
    )

    //Структура гео-данных
    data class KDNode(
        val unit: GeoUnit,
        var nodeLeft: KDNode?,
        var nodeRight: KDNode?
    )

    var depth = 0

    var tree: KDNode? = null


    fun getNode(currentUnit: KDNode, newUnit: GeoUnit) {
        val latOrLng = depth % 2
        println(latOrLng)
        if (latOrLng == 0) {//lat
            println("широта ${newUnit.id}")
            if (currentUnit.unit.cords.lat > newUnit.cords.lat) {
                //Идем левую ноду
                if (currentUnit.nodeLeft == null) {
                    currentUnit.nodeLeft = KDNode(newUnit, null, null)
                    return
                } else {
                    depth++
                    getNode(currentUnit.nodeLeft!!, newUnit)
                }
            } else {
                // Идем в правую ноду
                if (currentUnit.nodeRight == null) {
                    currentUnit.nodeRight = KDNode(newUnit, null, null)
                    return
                } else {
                    depth++
                    getNode(currentUnit.nodeRight!!, newUnit)
                }

            }
        } else {//lng
            println("долгота ${newUnit.id}")

            if (currentUnit.unit.cords.lng > newUnit.cords.lng) {
                //Идем левую ноду
                if (currentUnit.nodeLeft == null) {
                    currentUnit.nodeLeft = KDNode(newUnit, null, null)
                    return
                } else {
                    depth++
                    getNode(currentUnit.nodeLeft!!, newUnit)
                }
            } else {
                // Идем в правую ноду
                if (currentUnit.nodeRight == null) {
                    currentUnit.nodeRight = KDNode(newUnit, null, null)
                    return
                } else {
                    depth++
                    getNode(currentUnit.nodeRight!!, newUnit)
                }
            }
        }
    }


    //    1. Добавление географического объекта
    fun insertUnit(point: GeoUnit) {
        //Корень дерева
        depth = 0
        if (tree == null) {
            tree = KDNode(point, null, null)
            return
        }
        getNode(tree!!, point)
    }

    //    2. Поиск близких координат
    fun findNearest() {}
}