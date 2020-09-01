package provider_widget_generator.builder

import provider_widget_generator.model.Widget
import com.google.common.base.CaseFormat
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

object TemplateStatefulWidgetBuilder {

    enum class Template {
        StatefulWidget, Model
    }

    private object Properties {
        const val ClassName = "CLASS_NAME"
        const val ClassNameLower = "CLASS_NAME_LOWER"
    }

    fun build(widget: Widget, project: Project, destinationDirectory: PsiDirectory) {
        val manager = FileTemplateManager.getInstance(project)
        val properties = buildProperties(manager.defaultProperties, widget)

        mapTemplates(widget).forEach { template ->
            val fileTemplate = manager.getInternalTemplate(template.key.name.toLowerCase())
            FileTemplateUtil.createFromTemplate(fileTemplate, template.value, properties, destinationDirectory)
        }
    }

    private fun mapTemplates(widget: Widget) = Template.values().associate {
        when (it) {
            Template.StatefulWidget -> Pair(it, widget.screenFilename)
            Template.Model -> Pair(it, widget.modelFilename)
        }
    }

    private fun buildProperties(properties: java.util.Properties, widget: Widget) = properties.apply {
        put(Properties.ClassName, widget.className)
        put(Properties.ClassNameLower, CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, widget.name))
    }

}