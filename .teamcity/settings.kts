import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {

    vcsRoot(HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster)

    buildType(Build)
    buildType(ChromeTest)
    buildType(FirefoxTest)
    buildType(Publishing)

//    since 2019.2 versions
    sequential {
      buildType(Build)
      parallel(options = {onDependencyFailure = FailureAction.CANCEL}) {
        buildType(ChromeTest)
        buildType(FirefoxTest)
      }
      buildType(Publishing)
    }
}

object Build : BuildType({
    name= "Build"
    description = "Build description added here"

    vcs {
        root(HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster)
    }

    steps {
      script {
        name = "Install"
        scriptContent = "npm install"
      }
      script {
        name = "Build"
        scriptContent = "echo build"
      }
    }

    features {
      swabra {  }
    }
})

object ChromeTest : BuildType({
  name= "ChromeTest"
  description = "ChromeTest"

  vcs {
    root(HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster)
  }

  steps {
    script {
      name = "ChromeTest"
      scriptContent = "exit 1"
    }
  }

  artifactRules = "coverage/my-dream-app => coverage.zip"
})

object FirefoxTest : BuildType({
  name= "FirefoxTest"
  description = "FirefoxTest"

  vcs {
    root(HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster)
  }

  triggers {
    vcs {
    }
  }

  steps {
    script {
      name = "FirefoxTest"
      scriptContent = "echo test-firefox"
    }
  }
})

object Publishing : BuildType({
  name= "Publishing"
  description = "Publishing"

  vcs {
    root(HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster)
  }

  triggers {
    vcs {
    }
  }

  steps {
    script {
      name = "Publishing"
      scriptContent = "echo publishing"
    }
  }

  dependencies{
    artifacts(ChromeTest){
      artifactRules = "coverage.zip"
    }
  }
})

object HttpsGithubComGopinathshivaAngularTeamcityKotlinDemoRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/gopinathshiva/angular-teamcity-kotlin-demo#refs/heads/master"
    url = "https://github.com/gopinathshiva/angular-teamcity-kotlin-demo"
})
