package provider_widget_generator.model

import com.google.common.base.CaseFormat

data class Widget(
    val name: String,
    val className: String,
    val projectName: String,
    val modelFilename: String,
    val screenFilename: String

) {

    companion object {
        fun build(name: String, projectName: String): Widget {
            return Widget(
                name,
                CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name),
                projectName,
                "${name}_model.dart",
                "${name}.dart"
            )
        }
    }

}