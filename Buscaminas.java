import java.util.Random;
import java.util.Scanner;

class Buscaminas
{
    /*
        no / flag / si
        -9 / -90 / 9 = mina
        -11 / -110 / 0 = 0
        -i / -(10i) / i = i
    */

    static int mines;
    
    static short EnsenyaTaulell(byte[][] taulell) {
        short comptador = 0;

        System.out.print("\n   ");
        for (int i = 0; i < taulell[0].length; i++) {
            System.out.printf("%3d", i+1);
        }

        System.out.println("");
        for (int i = 0; i < taulell.length; i++) {
            System.out.printf("%3d", i+1);
            for (int j = 0; j < taulell[i].length; j++) {
                if(taulell[i][j] == -11) System.out.print("  *");
                else if(taulell[i][j] < -9) { System.out.print("  F"); 
                comptador = taulell[i][j] == -90 ? comptador++ : (short)(mines + 1); }
                else if(taulell[i][j] < 0) System.out.print("  *");
                else if(taulell[i][j] == 9) { System.out.print("  X"); comptador = -127; } //Si és una mina, perdem.
                else System.out.print("  " + taulell[i][j]);

                if(taulell[i][j] == -90) comptador++;
            }
            System.out.println("");
        }
        return comptador;
    }
    
    static byte[][] ValorsTotals(byte[][] taulell, int mines)
    {
        //Crea els valors al voltant de les mines. 
        short comptador = 0;
        for (int i = 0; i < taulell.length; i++) { //Només mira les mines.
            if(comptador >= mines) break;
            for (int j = 0; j < taulell[i].length; j++) {
                
                if(taulell[i][j] != -9) continue; //Si no és una mina, no ho mirem.
                else if(++comptador > mines) break;

                if(i > 0)
                {
                    if(j > 0)
                    {
                        if(Math.abs(taulell[i-1][j-1]) != 9) 
                            taulell[i-1][j-1]--; //i-1 j-1
                    }

                    if(Math.abs(taulell[i-1][j]) != 9) 
                        taulell[i-1][j]--;//i-1

                    if(j < taulell[i].length - 1)
                    {
                        if(Math.abs(taulell[i-1][j+1]) != 9) 
                            taulell[i-1][j+1]--;//i-1 j+1
                    }
                }

                if(j > 0)
                {
                    if(Math.abs(taulell[i][j-1]) != 9) 
                        taulell[i][j-1]--;//j-1
                }

                if(j < taulell[i].length - 1)
                {
                    if(taulell[i][j+1] != -9) 
                        taulell[i][j+1]--;//j+1
                }

                if(i < taulell.length - 1)
                {

                    if(j > 0)
                    {
                        if(taulell[i+1][j-1] != -9) 
                            taulell[i+1][j-1]--;//i+1 j-1
                    }

                    if(taulell[i+1][j] != -9) 
                        taulell[i+1][j]--;//i+1 

                    if(j < taulell[i].length - 1)
                    {
                        if(taulell[i+1][j+1] != -9) 
                            taulell[i+1][j+1]--;//i+1 j+1
                    }
                }
            }
        }

        return taulell;
    }

    static byte[][] esZero(byte[][] taulell, byte x, byte y) {
        //Si hi ha un zero, descobreix totes les caselles del voltant. També ho fa si el jugador torna a seleccionar una casella descoberta. Ignora banderes.
        if(taulell[x][y] == -11) taulell[x][y] = 0;

        for (int i = 0; i < 8; i++) {
            if(x > 0)
            {
                if(y > 0)
                {
                    if(taulell[x-1][y-1] == -11) 
                        taulell = esZero(taulell, (byte)(x-1), (byte)(y-1));
                    else if(taulell[x-1][y-1] < 0 && taulell[x-1][y-1] % 10 != 0)
                        taulell[x-1][y-1]*=-1; //i-1 j-1
                }

                if(taulell[x-1][y] == -11) 
                    taulell = esZero(taulell, (byte)(x-1), y);
                else if(taulell[x-1][y] < 0 && taulell[x-1][y] % 10 != 0)
                    taulell[x-1][y]*=-1;//i-1

                if(y < taulell[0].length - 1)
                {
                    if(taulell[x-1][y+1] == -11) 
                        taulell = esZero(taulell, (byte)(x-1), (byte)(y+1));
                    else if(taulell[x-1][y+1] < 0 && taulell[x-1][y+1] % 10 != 0)
                        taulell[x-1][y+1]*=-1;//i-1 j+1
                }
            }

            if(y > 0)
            {
                if(taulell[x][y-1] == -11) 
                    taulell = esZero(taulell, x, (byte)(y-1));
                else if(taulell[x][y-1] < 0 && taulell[x][y-1] % 10 != 0)
                    taulell[x][y-1]*=-1;//j-1
            }

            if(y < taulell[0].length - 1)
            {
                if(taulell[x][y+1] == -11) 
                    taulell = esZero(taulell, x, (byte)(y+1));
                else if(taulell[x][y+1] < 0 && taulell[x][y+1] % 10 != 0)
                    taulell[x][y+1]*=-1;//j+1
            }

            if(x < taulell.length - 1)
            {

                if(y > 0)
                {
                    if(taulell[x+1][y-1] == -11) 
                        taulell = esZero(taulell, (byte)(x+1), (byte)(y-1));
                    else if(taulell[x+1][y-1] < 0 && taulell[x+1][y-1] % 10 != 0)
                        taulell[x+1][y-1]*=-1;//i+1 j-1
                }

                if(taulell[x+1][y] == -11) 
                    taulell = esZero(taulell, (byte)(x+1), y);
                else if(taulell[x+1][y] < 0 && taulell[x+1][y] % 10 != 0)
                    taulell[x+1][y]*=-1;//i+1 

                if(y < taulell.length - 1)
                {
                    if(taulell[x+1][y+1] == -11) 
                        taulell = esZero(taulell, (byte)(x+1), (byte)(y+1));
                    else if(taulell[x+1][y+1] < 0 && taulell[x+1][y+1] % 10 != 0)
                        taulell[x+1][y+1]*=-1;//i+1 j-1
                }
            }
        }
        return taulell;
    }
    public static void main(String[] args) {

        byte[][] taulell;
        byte files, columnes;
        float percentatgeMines = 0.15f;
        Scanner in = new Scanner(System.in);

        System.out.print("Escriu el nombre de files: ");
        files = in.nextByte();

        System.out.print("Escriu el nombre de columnes: ");
        columnes = in.nextByte();

        in.nextLine();

        mines = (int) Math.round(files * columnes * percentatgeMines);
        taulell = new byte[files][columnes];

        //Coloquem les mines.
        for (int i = 0; i < mines; i++) {
            Random r = new Random();
            byte filaRandom = (byte) r.nextInt(files);
            byte columnaRandom = (byte) r.nextInt(columnes);
            if(taulell[filaRandom][columnaRandom] == 0)
            {
                taulell[filaRandom][columnaRandom] = -9;
            }
            else
                i--; //Si ja hi havia una mina, reiniciem l'intent. Per no perdre mines.
        }
        
        taulell = ValorsTotals(taulell, mines);

        //Comprova els 0s que queden.
        for (int i = 0; i < taulell.length; i++) {
            for (int j = 0; j < taulell[i].length; j++) {
                if(taulell[i][j] == 0) taulell[i][j] = -11;
            }
        }


        
        System.out.printf("\nHi ha %d mines.\n", mines);
        EnsenyaTaulell(taulell);
        
        boolean win=false, lose=false;
        while (!win && !lose) {
            System.out.print("\n\n Intent (Format: \"(F) fila columna\") (escriu \"exit\" per sortir): ");
            String intent = in.nextLine().toUpperCase();
            byte intentFila =-1, intentColumna=-1;
            if(intent.equals("EXIT")) break;

            String[] intentSplit = intent.split(" ");

            boolean flag = intentSplit[0].equals("F"), error = false;
            
            for (int i=0;i<intentSplit.length;i++) { 
                if(i==0 && flag) i++;

                if(intentColumna == -1) {
                    try {
                        intentColumna = (byte) (Integer.parseInt(intentSplit[i])-1);
                    } catch (Exception e) {
                        error = true;
                        break;
                    }
                } else {
                    try {
                        intentFila = (byte) (Integer.parseInt(intentSplit[i])-1);
                    } catch (Exception e) {
                        error = true;
                        break;
                    }
                }
            }
            if(intentFila >= taulell.length || intentColumna >= taulell[0].length || intentFila < 0 || intentColumna < 0) error = true;
            if(error) { System.err.println("Entrada invàlida"); continue; }

            if(flag) {
                if(taulell[intentFila][intentColumna] < 0) { //Només podem marcar si no hem descobert la cel·la.
                    if(taulell[intentFila][intentColumna] % 10 != 0) //Si no està marcat
                        taulell[intentFila][intentColumna]*=10;
                    else { //Si ja està marcat.
                        taulell[intentFila][intentColumna]/=10;
                    }
                }
            } else {
                if(taulell[intentFila][intentColumna] == -11 || taulell[intentFila][intentColumna] > 0) { taulell = esZero(taulell, intentFila, intentColumna);}
                else if(taulell[intentFila][intentColumna] % 10 != 0 && taulell[intentFila][intentColumna] < 0) taulell[intentFila][intentColumna]*=-1;

                if(taulell[intentFila][intentColumna] == 9) { lose = true; break; }
            }
           
            short minesAlTaulell = EnsenyaTaulell(taulell);
            System.out.printf("\nHi ha %d mines.\n", mines);

            lose = minesAlTaulell < 0;
            win = mines == minesAlTaulell;
        }

        in.close();
        EnsenyaTaulell(taulell);

        if(lose) System.out.println("Has perdut!");
        else if(win) System.out.println("Has guanyat!");
        else System.out.println("Tancant programa...");
    }
}