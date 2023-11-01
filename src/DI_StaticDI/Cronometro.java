// package DI_StaticDI;

// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import javax.swing.Timer;

// /**
//  *
//  * @author Higor
//  */
// public class Cronometro {

//     private static Timer timer;
//     private static ActionListener action;
//     private static int minutos;
//     private static int segundos;

//     public static void go(boolean cond) {
//         if (cond == true) {
//             action = new ActionListener() {
//                 public void actionPerformed(ActionEvent e) {
//                     System.out.println("0" + minutos + " : " + ++segundos + "");
//                     if (segundos == 59) {
//                         segundos = 0;
//                         minutos = 1;
//                     }
//                 }
//             };
//             timer = new Timer(1000, action);
//             timer.start();
//         } else {
//             System.out.println("Resultado.");
//             timer.stop();
//             /**
//              * Gera erro aqui, quando eu chamo a função go com parâmetro false.
//              * Que seria para encerrar o temporizador.
//              */
//         }
//     }
// }