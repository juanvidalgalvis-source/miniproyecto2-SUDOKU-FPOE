# TODO - Sudoku MP1 (tareas 0.4 a 0.7)

## 0.4 Esqueleto del controlador principal
- [ ] Implementar `GameController` (placeholders: selección, ingreso de número, ayuda, validación visual)
- [ ] Conectar eventos GUI -> modelo (sin lógica en FXML)

## 0.5 Sistema orientado a eventos mediante interfaces
- [ ] Crear interfaz(es) `GameEventHandler` y/o `CellInteractionHandler`
- [ ] Definir métodos: click de celda, entrada teclado, validación

## 0.6 Clases internas y adaptadores
- [ ] Implementar una clase interna (inner class)
- [ ] Implementar al menos una clase adaptadora (keyboard o mouse)
- [ ] Integrar eventos JavaFX: mouse y teclado

## 0.7 Base visual inicial del tablero
- [ ] Actualizar `sudoku-view.fxml` con `GridPane` 6x6
- [ ] Crear componentes visuales de celda
- [ ] Configurar layouts `VBox`/`HBox`/`GridPane`
- [ ] Preparar CSS básico y paleta inicial

## Verificación
- [ ] Compilar con `mvn -q test -DskipTests=false`

