import java.util.Scanner;

public class Othello {


    static byte[][] taulell;

    public static void main(String[] args) {
        othello();
    }

    public static void othello() {
        taulell = CrearTaulell();
        Scanner in = new Scanner(System.in);
        boolean torn = true; // torn = B, !torn = N
        byte tornsInvalids=0;

        boolean jocAcabat = false;
        do {
            torn = !torn;
            char tornChar = torn ? 'B' : 'N';


            ComprovaEntradesValides(torn);

            if(EnsenyaTaulell()) { //EnsenyaTaulell() retorna si hi ha torns vàlids.
                tornsInvalids = 0;
                boolean posicioValida;
                byte fila, columna;

                System.out.println("Torn de " + tornChar);
                do {
                    String posicio = EntradaPosicio(in);
                    fila = (byte)(posicio.charAt(1)-'1');
                    columna = (byte)(posicio.charAt(0)-'A');

                    posicioValida = taulell[fila][columna] == 2;
                } while (!posicioValida);

                taulell[fila][columna] = torn ? (byte)1 : (byte)-1;
                CanviaFitxes(fila, columna, torn);

            } else {
                System.out.println("Torn invàlid per a " + tornChar + "! Premeu INTRO per continuar.");
                in.nextLine();
                if(++tornsInvalids >= 2) {
                    System.out.println("\nCap jugador té moviments vàlids: s'ha acabat el joc!");
                    jocAcabat = true;
                }
            }

        } while (!jocAcabat);
        in.close();

        byte blanques = 0, negres = 0;
        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell[i].length; j++) {
                if(taulell[i][j] == 1) {
                    blanques++;
                } else if(taulell[i][j] == -1) {
                    negres++;
                }
            }
        }

        if(blanques > negres) {
            System.out.println("Han guanyat les blanques!");
        } else if(blanques < negres) {
            System.out.println("Han guanyat les negres!");
        } else {
            System.err.println("Heu empatat!");
        }

    }


    static byte[][] CrearTaulell() {
        byte[][] taulell = new byte[8][8];

        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell.length; j++) {
                System.out.printf("%2d,%d", i, j);
            }
            System.out.println();
        }
        taulell[3][3]=1;
        taulell[3][4]=-1;
        taulell[4][3]=-1;
        taulell[4][4]=1;

        return taulell;
    }

    static String EntradaPosicio(Scanner in) {
        boolean valid;
        String entrada;
        do {
            System.out.print("Escriu una posició: ");
            valid = true;
            entrada = in.next().toUpperCase();
            in.nextLine();

            if(entrada.length() != 2) {
                valid = false;
                continue;
            }

            valid = !(entrada.charAt(0) > 'H' || entrada.charAt(0) < 'A'
                   || entrada.charAt(1) > '8' || entrada.charAt(1) < '1');
            if(!valid) {
                System.out.println("Entrada invàlida.");
            }
        } while (!valid);

        return entrada;
    }

    //#region Comprovació d'entrades vàlides

    static void ComprovaEntradesValides(boolean torn) {
        /*
        Trobar una peça del jugador i comprovar les 8 direcciones al seu voltant.
        Si alguna és una peça del jugador contrari continua en aquella direcció fins trobar o una peça del jugador que li toqui, el buit o la fi del taulell.
        Si és el buit, és vàlid i per tant canvia el lloc a 2. Si no, no edita res.

            Direccions:
                        ↑
                  -------------
                  | 1 | 2 | 3 |
                  -------------
                ← | 4 | x | 5 | →
                  -------------
                  | 6 | 7 | 8 |
                  -------------
                        ↓
        */

        //Reinicia els llocs vàlids.
        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell[i].length; j++) {
                if(taulell[i][j] == 2) taulell[i][j] = 0;
            }
        }
        byte jugador = torn ? (byte)1 : (byte)-1; //1 = blanca, -1 = negra

        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell[i].length; j++) {
                if(taulell[i][j] == jugador) {
                    ComprovacioFitxa(i, j, jugador);
                }
            }
        }

    }

    static void ComprovacioFitxa(int i, int j, byte jugador) {
        jugador*=-1;
        if(i > 0) {
            if(j > 0 && taulell[i-1][j-1] == jugador) TiraMillas(i-1, j-1, 1, jugador);

            if(taulell[i-1][j] == jugador) TiraMillas(i-1, j, 2, jugador);

            if(j < taulell[0].length-1 && taulell[i-1][j+1] == jugador) TiraMillas(i-1, j+1, 3, jugador);
        }

        if(j > 0 && taulell[i][j-1] == jugador) TiraMillas(i, j-1, 4, jugador);

        if(j < taulell[0].length-1 && taulell[i][j+1] == jugador) TiraMillas(i, j+1, 5, jugador);

        if(i < taulell.length-1) {
            if(j > 0 && taulell[i+1][j-1] == jugador) TiraMillas(i+1, j-1, 6, jugador);

            if(taulell[i+1][j] == jugador) TiraMillas(i+1, j, 7, jugador);

            if(j < taulell[0].length-1 && taulell[i+1][j+1] == jugador) TiraMillas(i+1, j+1, 8, jugador);
        }
    }

    static boolean TiraMillas(int fila, int columna, int moviment, byte objectiu) {
        int filaNova = fila, columnaNova = columna;
        if(moviment >= 1 && moviment <= 3) {
            filaNova--;
        } else if(moviment >= 6 && moviment <= 8){
            filaNova++;
        }
        if(moviment == 1 || moviment == 4 || moviment == 6) {
            columnaNova--;
        } else if(moviment == 3 || moviment == 5 || moviment == 8) {
            columnaNova++;
        }
/*
        switch (moviment) { //Files
            case 1, 2, 3:
                filaNova--;
                break;
            case 6, 7, 8:
                filaNova++;
                break;
            default:
                break;
        }

        switch (moviment) { //Columnes
            case 1, 4, 6:
                columnaNova--;
                break;
            case 3, 5, 8:
                columnaNova++;
            default:
                break;
        }
        */
        boolean posicioValida = filaNova >= 0 && filaNova < taulell.length
             && columnaNova >= 0 && columnaNova < taulell[0].length;

        if(posicioValida) {
            if(taulell[filaNova][columnaNova] == objectiu) {
                return TiraMillas(filaNova, columnaNova, moviment, objectiu);
            }

            if(taulell[filaNova][columnaNova] == 0) {
                taulell[filaNova][columnaNova] = 2;
            }

            return taulell[filaNova][columnaNova] == -objectiu;
        }
        return false;
    }

    //#endregion

    //#region Gir de les fitxes
    static void CanviaFitxes(byte i, byte j, boolean torn) {
        byte jugador = torn ? (byte)-1 : (byte)1;

        if(i > 0) {
            if(j > 0 && taulell[i-1][j-1] == jugador && TiraMillas(i-1, j-1, 1, jugador)) GiraFitxes(i-1, j-1, 1, jugador);

            if(taulell[i-1][j] == jugador && TiraMillas(i-1, j, 2, jugador)) GiraFitxes(i-1, j, 2, jugador);

            if(j < taulell[0].length-1 && taulell[i-1][j+1] == jugador && TiraMillas(i-1, j+1, 3, jugador)) GiraFitxes(i-1, j+1, 3, jugador);
        }

        if(j > 0 && taulell[i][j-1] == jugador && TiraMillas(i, j-1, 4, jugador)) GiraFitxes(i, j-1, 4, jugador);

        if(j < taulell[0].length-1 && taulell[i][j+1] == jugador && TiraMillas(i, j+1, 5, jugador)) GiraFitxes(i, j+1, 5, jugador);

        if(i < taulell.length-1) {
            if(j > 0 && taulell[i+1][j-1] == jugador && TiraMillas(i+1, j-1, 6, jugador)) GiraFitxes(i+1, j-1, 6, jugador);

            if(taulell[i+1][j] == jugador && TiraMillas(i+1, j, 7, jugador)) GiraFitxes(i+1, j, 7, jugador);

            if(j < taulell[0].length-1 && taulell[i+1][j+1] == jugador && TiraMillas(i+1, j+1, 8, jugador)) GiraFitxes(i+1, j+1, 8, jugador);
        }
    }

    static void GiraFitxes(int fila, int columna, int direccio, byte color) {
         int filaNova = fila, columnaNova = columna;
         if(direccio >= 1 && direccio <= 3) {
            filaNova--;
        } else if(direccio >= 6 && direccio <= 8){
            filaNova++;
        }
        if(direccio == 1 || direccio == 4 || direccio == 6) {
            columnaNova--;
        } else if(direccio == 3 || direccio == 5 || direccio == 8) {
            columnaNova++;
        }
/*
        switch (direccio) { //Files
            case 1, 2, 3:
                filaNova--;
                break;
            case 6, 7, 8:
                filaNova++;
                break;
            default:
                break;
        }

        switch (direccio) { //Columnes
            case 1, 4, 6:
                columnaNova--;
                break;
            case 3, 5, 8:
                columnaNova++;
            default:
                break;
        }
*/
        if(fila >= 0 && fila < taulell.length && columna >= 0 && columna < taulell[0].length) {
            if(taulell[fila][columna] == color) {
                taulell[fila][columna] *=-1;
                GiraFitxes(filaNova, columnaNova, direccio, color);
            }
        }
    }

    //#endregion

    static boolean EnsenyaTaulell() {
        boolean tornValid=false;

        System.out.println("\n   A B C D E F G H");
        for (int i= 0; i < taulell.length; i++) {
            System.out.printf("%2d", i+1);
            for (int j = 0; j < taulell.length; j++) {
                char fitxa;
                switch (taulell[i][j]) {
                    case 1 : fitxa='B'; break;  //Blanca
                    case -1 : fitxa='N'; break; //Negra
                    case 2 : fitxa='+';         //Posició vàlida
                        tornValid=true; break;
                    default : fitxa='-';        //Posició invàlida
                }
                System.out.print(" " + fitxa);
            }
            System.out.println();
        }

        return tornValid;
    }
}
