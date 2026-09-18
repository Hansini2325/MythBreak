@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM Project: MythBreak – Maven Wrapper for Windows

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$p='%~f0';$t=[IO.Path]::GetTempFileName();[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12;&([scriptblock]::Create([IO.File]::ReadAllText($p.Replace('\.mvn','\').Replace($p.Split('\')[-1],'') + '.mvn\wrapper\MavenWrapperDownloader.ps1')));exit}" 2>&1`) DO (
  IF "%%A"=="MVN_CMD" SET __MVNW_CMD__=%%B
  IF "%%A"=="MVN_ERROR" SET __MVNW_ERROR__=%%B
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
@IF NOT "%__MVNW_ERROR__%"=="" (
  ECHO. >&2
  ECHO ERROR - %__MVNW_ERROR__% >&2
  ECHO. >&2
  EXIT /B 1
)
@IF "%__MVNW_CMD__%"== "" (
  ECHO. >&2
  ECHO ERROR - could not download maven wrapper >&2
  ECHO. >&2
  EXIT /B 1
)
@%__MVNW_CMD__% %*
