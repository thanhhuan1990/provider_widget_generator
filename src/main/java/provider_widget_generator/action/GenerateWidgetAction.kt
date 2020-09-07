package provider_widget_generator.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import provider_widget_generator.builder.TemplateBuilder
import provider_widget_generator.builder.TemplateStatefulWidgetBuilder
import provider_widget_generator.model.Widget
import provider_widget_generator.util.FlutterUtils

class GenerateWidgetAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: throw IllegalStateException("Cannot find project")
        val projectName = FlutterUtils.readProjectName(project)
            ?: throw IllegalStateException("Cannot find flutter project name")

        val input = Messages.showInputDialogWithCheckBox(
            "Widget Name",
            "New Provider Stateless / Stateful Widget",
            "StatefulWidget",
            false,
            true,
            null,
            "",
            SimpleClassNameInputValidator()
        )

        if (input.first.isBlank()) {
            return
        }

        val widget = Widget.build(input.first, projectName)

        val directory = event.getData(LangDataKeys.PSI_ELEMENT) as PsiDirectory
        if (directory.findSubdirectory(widget.name) != null) {
            Messages.showErrorDialog("File already exists", "Flutter Provider Widget")
            return
        }

        WriteCommandAction.runWriteCommandAction(event.project) {
            val widgetDirectory = directory.createSubdirectory(widget.name)
            if(input.second) {
                TemplateStatefulWidgetBuilder.build(widget, project, widgetDirectory)
            } else {
                TemplateBuilder.build(widget, project, widgetDirectory)
            }
        }
    }
}