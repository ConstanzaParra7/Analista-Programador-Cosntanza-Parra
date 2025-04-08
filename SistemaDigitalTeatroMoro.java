/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistemadigitalteatromoro;

import java.util.Scanner;

/**
 *
 * @author conam
 */
public class SistemaDigitalTeatroMoro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);
       
        //Declaro variables a utilizar
        int opcionElegida, tarifaVIP, tarifaPlateaAlta, tarifaPlateaBaja, tarifaPalco;
        int tipoEntrada, tarifaSeleccionada, cantidadEntradas, edad, subTotal;
        boolean filaValida, otraEntrada;
        String tipoSeleccionado, filaSeleccionada, respuestaEntrada;
        double descuentoEdad, total, totalFinal;
        
        
        //Inicializo variables a utilizar
        tarifaVIP = 30000;
        tarifaPlateaAlta = 18000;
        tarifaPlateaBaja = 15000;
        tarifaPalco = 13000;
        tarifaSeleccionada = 0;
        tipoSeleccionado = "";
        cantidadEntradas = 1;
        descuentoEdad = 0.0;
        filaSeleccionada = "";
        filaValida = false;
        otraEntrada = true;
        totalFinal= 0;
        
        
        //E. de control Bucle FOR principal, para que mi usuario inicie sus compra
        for (;;) {//condición vacía para que se repita hasta que elija una condición indida
            System.out.println("==============================");
            System.out.println("Hola! bienvenido al TEATRO MORO");
            System.out.println("Indícanos la acción que quieras realizar: ");
            System.out.println("");
            System.out.println("1 - Comprar entrada");
            System.out.println("2 - Salir");
            System.out.println("==============================");
            opcionElegida = scanner.nextInt();
            
            
             try {
            Thread.sleep(2000); //Indico esperar 2 segundos
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
             
            //E. condicional SWITCH en caso de que el usuario se dirija a comprar entradas
            switch (opcionElegida) {
                case 1:
                    do { //E. de control DO-WHILE para poder agregar mas entradas
                        System.out.println("Has seleccionado comprar una entrada");
                        System.out.println("Selecciona una de las siguientes opciones disponibles: ");
                        System.out.println("1 - VIP $" + tarifaVIP);
                        System.out.println("2 - PLATEA ALTA $" + tarifaPlateaAlta);
                        System.out.println("3 - PLATEA BAJA $" + tarifaPlateaBaja);
                        System.out.println("4 - Palco $" + tarifaPalco);
                        System.out.println("");
                    
                        tipoEntrada = scanner.nextInt();
                    
                    
                        //E. condicional SWITCH para las opciones elejidas
                        switch (tipoEntrada) {
                            case 1:
                            tarifaSeleccionada = tarifaVIP;
                            tipoSeleccionado = "VIP";
                            break;
                            
                            case 2:
                            tarifaSeleccionada = tarifaPlateaAlta;
                            tipoSeleccionado = "PLATEA ALTA";
                            break;
                            
                            case 3:
                            tarifaSeleccionada = tarifaPlateaBaja;
                            tipoSeleccionado = "PLATEA BAJA";
                            break;
                            
                            case 4:
                            tarifaSeleccionada = tarifaPalco;
                            tipoSeleccionado = "PALCO";
                            break;
                            
                            default:
                            System.out.println("Opción inválida, volviendo al menú principal");
                            continue;
                        }
                    
                    try {
            Thread.sleep(2000); //Indico esperar 2 segundos
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
                        // E. de control DO-WHILE para la selección de fila disponible
                        do {
                            System.out.println("Selecciona la fila que prefieras (A, B, C o D); ");
                            filaSeleccionada = scanner.next().toUpperCase();
                        
                            if (filaSeleccionada.equals("A")|| filaSeleccionada.equals("B") || filaSeleccionada.equals("C") || filaSeleccionada.equals("D")) {
                            filaValida = true;
                            } else {
                                System.out.println("Fila no válida, intente nuevamente");
                            }
                        } while (!filaValida);
                        System.out.println("");
                        
                         try {
            Thread.sleep(2000); //Indico esperar 2 segundos
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
                        
                        //Estructuta de control WHILE para que se calcule el precio final solo si la edad es válida
                        while (true) {
                            System.out.println("Por favor, ingresa tu edad: ");
                           //creo un if para verificar que mi entrada es entero
                            if (scanner.hasNextInt()) {
                                edad = scanner.nextInt();
                                if (edad > 0) break;
                                else System.out.println("Edad debe ser mayor a 0");
                            } else {
                                System.out.println("Edad no válida, intente nuevamente");
                                scanner.nextInt();
                            }
                        }
                        
                    
                        //E. condicional IF-ELSE para definir canidad de descuento a aplicar
                        if (edad < 18) {
                            descuentoEdad = 0.10; //descuento del 10
                        } else if (edad >= 65) {
                            descuentoEdad = 0.15;
                        } else {
                            descuentoEdad = 0.0;
                        }
                    
                        subTotal = tarifaSeleccionada;
                        total = subTotal - (subTotal*descuentoEdad);
                        totalFinal += total; //indico que se acumulará el total de cada compra
                    
                    
                    try {
            Thread.sleep(2000); //Indico esperar 2 segundos
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
                    
                    
                        //Resumen de la compra en base a todo lo seleccionado
                        System.out.println("====================================");
                        System.out.println("Resumen de tu compra:");
                        System.out.println("Tipo de entrada: " + tipoSeleccionado);
                        System.out.println("Precio base: " + subTotal);
                        System.out.println("Ubicación: Fila "+ filaSeleccionada);
                    
                        if (descuentoEdad >0){
                            System.out.println("Descuento Aplicado: " + (descuentoEdad*100) + "%");
                        } else {
                            System.out.println("No se aplicó descuento");
                        }
                        System.out.println("");
                        System.out.println("Total a pagar por esta entrada: " + total);
                        System.out.println("====================================");
                    
                        //Estructura condicional IF-ELSE para agregar otra entrada a la venta
                        System.out.println("¿Desea agregar otra entrada (Si/No)");
                        respuestaEntrada = scanner.next();
                        otraEntrada = respuestaEntrada.equalsIgnoreCase("Si");
                        
                    } while (otraEntrada); //si el usuario responde "Si" el ciclo se repite
                    
                     try {
            Thread.sleep(2000); //Indico esperar 2 segundos
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
                    
                    //Indico resumen final de todas las compras
                    System.out.println("====================================");
                    System.out.println("Resumen de tu compra:");
                    System.out.println("Total entradas compradas: $" + totalFinal);
                    System.out.println("¡Gracias por preferir al TEATRO MORO");
                     System.out.println("====================================");
                   
                    break;//break de mi caso 1
                    
                    
                case 2:
                    System.out.println("Gracias por preferir al TEATRO MORO");
                    return; //
                default:
                    System.out.println("Opción no válida. Por favor, intenta nuevamente.");
            }
                    
        }
    }
}

