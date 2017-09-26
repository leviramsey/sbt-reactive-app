/*
 * Copyright 2017 Lightbend, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lightbend.sbtreactiveapp

import sbt._
import scala.collection.immutable.Seq

import Keys._

sealed trait App extends SbtReactiveAppKeys {
  def projectSettings: Seq[Setting[_]] = Vector(
    nrOfCpus := None,
    diskSpace := None,
    memory := None,
    endpoints := Map.empty,
    volumes := Map.empty,
    privileged := false,
    healthCheck := None,
    readinessCheck := None,
    environmentVariables := Map.empty,
    reactiveLibProject := Some("basic"),
    reactiveLibVersion := Some("0.1.0-SNAPSHOT"),
    libraryDependencies ++= (
      for {
        project <- reactiveLibProject.value.toSeq
        version <- reactiveLibVersion.value.toSeq
      } yield "com.lightbend.reactive-lib" % project % version
    )
  )
}

case object LagomJavaApp extends App {
  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ Vector(
      reactiveLibProject := magic.Lagom.version.map(v => s"lagom${formatVersionMajorMinor(v)}")
    )
}

case object LagomScalaApp extends App {
  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ Vector(
      reactiveLibProject := magic.Lagom.version.map(v => s"lagom${formatVersionMajorMinor(v)}")
    )
}

case object LagomPlayJavaApp extends App {
  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ Vector(
      reactiveLibProject := magic.Lagom.version.map(v => s"lagom${formatVersionMajorMinor(v)}")
    )
}

case object LagomPlayScalaApp extends App {
  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ Vector(
      reactiveLibProject := magic.Lagom.version.map(v => s"lagom${formatVersionMajorMinor(v)}")
    )
}

case object PlayApp extends App {
  override def projectSettings: Seq[Setting[_]] =
    super.projectSettings ++ Vector(
      reactiveLibProject := magic.Play.version.map(v => s"play${formatVersionMajorMinor(v)}")
    )
}

case object BasicApp extends App

object App {
  def apply: App =
    if (magic.Lagom.isPlayJava)
      LagomPlayJavaApp
    else if (magic.Lagom.isPlayScala)
      LagomPlayScalaApp
    else if (magic.Lagom.isJava)
      LagomJavaApp
    else if (magic.Lagom.isScala)
      LagomScalaApp
    else if (magic.Play.isPlay)
      PlayApp
    else
      BasicApp
}