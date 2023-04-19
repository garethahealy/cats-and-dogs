{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "cats-and-dogs.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "cats-and-dogs.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "cats-and-dogs.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "cats-and-dogs.labels" -}}
helm.sh/chart: {{ include "cats-and-dogs.chart" . }}
{{ include "cats-and-dogs.selectorLabels" . }}
{{- if .Values.image_version }}
app.kubernetes.io/version: {{ .Values.image_version | quote }}
{{- else }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "cats-and-dogs.selectorLabels" -}}
app.kubernetes.io/name: {{ include "cats-and-dogs.name" . }}
app.kubernetes.io/component: {{ include "cats-and-dogs.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
deployment: {{ include "cats-and-dogs.fullname" . }}
{{- end -}}