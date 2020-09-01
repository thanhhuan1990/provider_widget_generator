package provider_widget_generator.action

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import com.intellij.openapi.actionSystem.*
import provider_widget_generator.builder.TemplateBuilder
import provider_widget_generator.model.Widget
import provider_widget_generator.util.FlutterUtils

class GenerateWidgetAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: throw IllegalStateException("Cannot find project")
        val projectName = FlutterUtils.readProjectName(project)
            ?: throw IllegalStateException("Cannot find flutter project name")

        val name = Messages.showInputDialog(
            "StatelessWidget Name",
            "New Provider Stateless Widget",
            null,
            null,
            SimpleClassNameInputValidator()
        )

        if (name?.isBlank() != false) {
            return
        }

        val widget = Widget.build(name, projectName)

        val directory = event.getData(LangDataKeys.PSI_ELEMENT) as PsiDirectory
        if (directory.findSubdirectory(widget.name) != null) {
            Messages.showErrorDialog("File already exists", "Flutter Provider Widget")
            return
        }

        WriteCommandAction.runWriteCommandAction(event.project) {
            val widgetDirectory = directory.createSubdirectory(widget.name)
            TemplateBuilder.build(widget, project, widgetDirectory)
        }
    }
}