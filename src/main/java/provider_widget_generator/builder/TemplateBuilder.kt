package provider_widget_generator.builder

import provider_widget_generator.model.Widget
import com.google.common.base.CaseFormat
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

object TemplateBuilder {

    enum class Template {
        Widget, Model
    }

    private object Properties {
        const val ClassName = "CLASS_NAME"
        const val ClassNameLower = "CLASS_NAME_LOWER"
    }

    fun build(bloc: Widget, project: Project, destinationDirectory: PsiDirectory) {
        val manager = FileTemplateManager.getInstance(project)
        val properties = buildProperties(manager.defaultProperties, bloc)

        mapTemplates(bloc).forEach { template ->
            val fileTemplate = manager.getInternalTemplate(template.key.name.toLowerCase())
            FileTemplateUtil.createFromTemplate(fileTemplate, template.value, properties, destinationDirectory)
        }
    }

    private fun mapTemplates(bloc: Widget) = Template.values().associate {
        when (it) {
            Template.Widget -> Pair(it, bloc.screenFilename)
            Template.Model -> Pair(it, bloc.modelFilename)
        }
    }

    private fun buildProperties(properties: java.util.Properties, bloc: Widget) = properties.apply {
        put(Properties.ClassName, bloc.className)
        put(Properties.ClassNameLower, CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, bloc.name))
    }

}