# Prompt generado con PromptZal

Modelo destino: claude-sonnet-4-6
Rol general: analista de datos
Formato de respuesta: markdown

## Agente: analista

Contexto: Analiza los datos de ventas

Variables disponibles:
- ventas: contenido del archivo "ventas.csv" (adjuntar el archivo)

Instrucciones:
1. Analiza ventas. Guarda el resultado como analisis.
2. Resume analisis en un maximo de 60 palabras. Guarda el resultado como resumen.

## Agente: reporte

Contexto: Genera un reporte ejecutivo

Variables disponibles:
- base: contenido del archivo "reporte.csv" (adjuntar el archivo)

Instrucciones:
1. Basandote en base, responde: Cuales son los principales hallazgos. Guarda la respuesta como hallazgos.

## Resultado solicitado

- resumen
- hallazgos
