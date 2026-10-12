package com.example.playground.engine

sealed class FlutterPropValue {
    data class StringVal(val value: String) : FlutterPropValue()
    data class NumberVal(val value: Double) : FlutterPropValue()
    data class BooleanVal(val value: Boolean) : FlutterPropValue()
    data class ColorVal(val colorHex: Long) : FlutterPropValue()
    data class EdgeInsetsVal(val top: Double, val start: Double, val bottom: Double, val end: Double) : FlutterPropValue()
    data class BorderRadiusVal(val radius: Double) : FlutterPropValue()
    data class AlignmentVal(val horizontal: String, val vertical: String) : FlutterPropValue()
    data class IconVal(val iconName: String) : FlutterPropValue()
    data class ListVal(val items: List<FlutterPropValue>) : FlutterPropValue()
    data class WidgetVal(val node: FlutterWidgetNode) : FlutterPropValue()
}

data class FlutterWidgetNode(
    val type: String,
    val line: Int = 1,
    val properties: Map<String, FlutterPropValue> = emptyMap(),
    val children: List<FlutterWidgetNode> = emptyList()
) {
    fun getString(key: String, default: String = ""): String {
        return (properties[key] as? FlutterPropValue.StringVal)?.value ?: default
    }

    fun getNumber(key: String, default: Double = 0.0): Double {
        return (properties[key] as? FlutterPropValue.NumberVal)?.value ?: default
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return (properties[key] as? FlutterPropValue.BooleanVal)?.value ?: default
    }

    fun getColor(key: String): Long? {
        return (properties[key] as? FlutterPropValue.ColorVal)?.colorHex
    }

    fun getEdgeInsets(key: String): FlutterPropValue.EdgeInsetsVal? {
        return properties[key] as? FlutterPropValue.EdgeInsetsVal
    }

    fun getBorderRadius(key: String): Double? {
        return (properties[key] as? FlutterPropValue.BorderRadiusVal)?.radius
    }

    fun getAlignment(key: String): FlutterPropValue.AlignmentVal? {
        return properties[key] as? FlutterPropValue.AlignmentVal
    }

    fun getIcon(key: String): String? {
        return (properties[key] as? FlutterPropValue.IconVal)?.iconName
    }

    fun getChild(key: String = "child"): FlutterWidgetNode? {
        val widgetVal = properties[key] as? FlutterPropValue.WidgetVal
        return widgetVal?.node ?: children.firstOrNull()
    }
}
