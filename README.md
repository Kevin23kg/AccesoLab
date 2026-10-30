# AccesoLab: Sistema de Control de Acceso a Laboratorios Basado en Lógica Proposicional

Proyecto desarrollado para la asignatura **Modelos Discretos para la Ingeniería** en la Universidad de las Fuerzas Armadas ESPE

## Integrantes
* Andy Chisaguano
* Kevin Vega

## Descripción del Proyecto
AccesoLab es una aplicación desarrollada en Java (con interfaz gráfica en Swing) que evalúa de forma automatizada las condiciones de acceso a un laboratorio universitario. 

El sistema implementa:
* **Proposiciones lógicas y tablas de verdad:** Validación de registros, reservas y horarios (P, Q, R).
* **Ley de De Morgan:** Conversión de negaciones para explicar de forma exacta los motivos de rechazo al usuario.
* **Motor de Inferencia:** Aplicación formal de reglas deductivas como *Modus Ponens* y *Modus Tollens*.

## Requisitos y Ejecución
1. Tener instalado **Java JDK** y **Maven**.
2. Clonar este repositorio o abrirlo en tu entorno de desarrollo favorito (como NetBeans o IntelliJ IDEA) compatible con Maven.
3. Ejecutar las pruebas unitarias y la clase principal del proyecto.
## Arquitectura de Selección y Evaluador Lógico

El motor de decisión implementa el patrón de expresiones booleanas para evaluar las tres condiciones principales de acceso:
* *P*: Usuario registrado en la base de datos local.
* *Q*: Reserva activa para la sala y franja horaria consultada.
* *R*: Rango de horario válido (08:00 a 17:00).

El sistema aplica equivalencia lógica estricta comprobada mediante pruebas de escritorio y automatizadas con JUnit.
