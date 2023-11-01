// package DI_StaticDI;

// import java.util.ArrayList;

// import javax.swing.Timer;

// public class TimerParada {
//     private static ArrayList<Timer> timerAtual = new ArrayList<>();
//     private static ArrayList<Timer> timerTotal = new ArrayList<>();

//     public static void implementTimer(int hora, int min, int seg) {
//         for (int i = 0; i < 19; i++) {
//         // Cria os timers
//         Timer timerAtualTemp = new Timer(0, null);
//         Timer timerTotalTemp = new Timer(0, null);

//         // Inicializa os timers com o tempo especificado
//         timerAtualTemp.setInitialDelay(hora * 3600 + min * 60 + seg);
//         timerTotalTemp.setInitialDelay(hora * 3600 + min * 60 + seg);

//         // Adiciona os timers às listas
//         timerAtual.add(timerAtualTemp);
//         timerTotal.add(timerTotalTemp);
//         }
//     }

//     public static Timer getTimerAtual(int id) {
//         // Imprime o tempo decorrido
//         System.out.println("Tempo decorrido sala"+ id +" : " + getTimerAtual(id).getDelay() + " segundos");
//         return timerAtual.get(id);
//     }

//     public static Timer getTimerTotal(int id) {
//         return timerTotal.get(id);
//     }

//     public static void StartTimer(int id) {
//         System.out.println("Start no timer " + id);
//         timerAtual.get(id).start();
//   }
// }
