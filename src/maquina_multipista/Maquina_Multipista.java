package maquina_multipista;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class Maquina_Multipista {

    public static void main(String[] args) {
        String ruta_archivo_transicisiones = "";
        String cadena_a_evaluar = "";
        int numero_de_pistas = 0;
        BufferedReader ingreso_por_consola = new BufferedReader(new InputStreamReader(System.in));
        try {
            ruta_archivo_transicisiones = ingreso_por_consola.readLine();
            cadena_a_evaluar = ingreso_por_consola.readLine();
            numero_de_pistas = Integer.parseInt(ingreso_por_consola.readLine());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Maquina_Multipista Maquina_Turing_MultiCinta = new Maquina_Multipista();
        File archivo = new File(ruta_archivo_transicisiones);
        try {
            FileReader lector_archivo = new FileReader(archivo);
            BufferedReader pistas = new BufferedReader(lector_archivo);
            String pista = pistas.readLine().toString();
            while (pista != null) {
                Maquina_Turing_MultiCinta.agregarTransicion(pista);
                pista = pistas.readLine();
            }
            lector_archivo.close();
            Maquina_Turing_MultiCinta.procesar(cadena_a_evaluar, numero_de_pistas);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Vector<String[]> transiciones = null;

    public Maquina_Multipista() {
        this.transiciones = new Vector<String[]>();
    }

    public void agregarTransicion(String nuevaTransicion) {
        String[] transicion = nuevaTransicion.split(",");
        this.transiciones.add(transicion);
    }

    public void procesar(String cadena_a_evaluar, int numero_de_pistas) {
        boolean cadena_valida = false;
        String log = "";
        String estado_actual = "q0";
        int index = 1;
        String[][] pistas = new String[numero_de_pistas][cadena_a_evaluar.length() + 2];
        pistas[0][0] = pistas[0][pistas[0].length - 1] = "blank";
        for (int i = 1; i < (pistas[0].length - 1); i++) {
            pistas[0][i] = cadena_a_evaluar.charAt(i - 1) + "";
        }
        for (int k = 1; k < (numero_de_pistas); k++) {
            for (int i = 0; i < (pistas[0].length); i++) {
                pistas[k][i] = "blank";
            }
        }
        while (!estado_actual.equals("qf") || !pistas[0][index].equals("blank")) {
            log += ("Estado: " + estado_actual);
            for (int j = 0; j < numero_de_pistas; j++) {
                log += "\tCinta " + (j + 1) + ": " + pistas[j][index] + "\tIndex: " + index;
            }
            log += "\n";
            int i;
            for (i = 0; i < this.transiciones.size(); i++) {
                if (this.transiciones.get(i)[0].equals(estado_actual)) {
                    boolean transicion_correcta = false;
                    int k;
                    for (k = 1; k <= numero_de_pistas; k++) {
                        if (this.transiciones.get(i)[k].equals(pistas[k - 1][index])) {
                            transicion_correcta = true;
                        } else {
                            k = numero_de_pistas + 1;
                            transicion_correcta = false;
                        }
                    }
                    if (transicion_correcta) {
                        for (k = 1; k <= numero_de_pistas; k++) {
                            estado_actual = this.transiciones.get(i)[1 + numero_de_pistas];
                            pistas[k - 1][index] = this.transiciones.get(i)[1 + numero_de_pistas + 1 + (k - 1)];
                            if (this.transiciones.get(i)[1 + numero_de_pistas + 1 + (k - 1) + 1].equals(">")) {
                                index += 1;
                            } else if (this.transiciones.get(i)[1 + numero_de_pistas + 1 + (k - 1) + 1].equals("<")) {
                                index -= 1;
                            }
                        }
                        break;
                    }
                }
            }
            if (i == this.transiciones.size()) {
                break;
            }
        }
        log += ("Estado: " + estado_actual);
        for (int j = 0; j < numero_de_pistas; j++) {
            log += "\tCinta " + (j + 1) + ": " + pistas[j][index] + "\tIndex: " + index;
        }
        log += "\n";
        if (estado_actual.equals("qf") && pistas[0][index].equals("blank")) {
            cadena_valida = true;
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        try {
            File file = new File("Resultados.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter fileWriter = new BufferedWriter(fw);
            bw.write(log);
            if (cadena_valida) {
                bw.write("\nComputo terminado");
                fileWriter.write("\nComputo terminado");
            } else {
                bw.write("\nComputo rechazado");
                fileWriter.write("\nComputo rechazado");
            }
            bw.flush();
            fileWriter.newLine();
            fileWriter.flush();
            bw.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
