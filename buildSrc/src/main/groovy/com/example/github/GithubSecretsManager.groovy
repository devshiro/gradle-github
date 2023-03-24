package com.example.github

import org.gradle.api.Plugin
import org.gradle.api.Project

class GithubSecretsManager implements Plugin<Project> {

	@Override
	void apply(Project project) {
		println "Hello from ${this.class.name}!"
	}
}
