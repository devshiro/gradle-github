package com.example.github

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class GithubSecretsManager implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.tasks.register('githubSecretsManagerTask', GithubSecretsManagerTask)
	}
}

abstract class GithubSecretsManagerTask extends DefaultTask {
	@TaskAction
	def action() {
		println "Hello from task action"
	}
}