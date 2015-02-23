package urlReader;

import gui.Messages;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import robot.TheRobot;

public class UrlResendHandler {

	URL myUrl;
	static boolean gotovo = false;

	public UrlResendHandler(URL url) {
		this.myUrl = url;
		gotovo = false;
	}

        public UrlResendHandler() {
		this.myUrl = null;
		gotovo = false;
	}

        
	public static void setGotovo() {
		gotovo = true;
	}

	public void run() {

		final TheRobot rob = new TheRobot();

		final Timer t = new Timer();

		TimerTask task = new TimerTask() {
			public void run() {
				try {
					if (gotovo)
						t.cancel();
					Scanner reader = new Scanner(myUrl.openStream());
					PrintWriter pw1 = new PrintWriter("newMessage.txt");
					while (reader.hasNextLine()) {
						pw1.write(reader.nextLine() + System.getProperty("line.separator"));
					}
					pw1.close();
					reader.close();
					File f = new File(Paths.get("oldMessage.txt").toString());
					if (!f.isFile()) {
						pw1 = new PrintWriter("oldMessage.txt");
						pw1.close();
					}
					List<String> listaStarih = Files.readAllLines(
							Paths.get("oldMessage.txt"),
							Charset.defaultCharset());
					List<String> listaNovih = Files.readAllLines(
							Paths.get("newMessage.txt"),
							Charset.defaultCharset());
					boolean isti = true;
					if (listaStarih.size() != listaNovih.size())
						isti = false;
					else
						for (int i = 0; i < listaNovih.size(); i++) {
							if (listaStarih.size() == 0) {
								isti = false;
								break;
							}
							if (!listaStarih.get(i).equals(listaNovih.get(i))) {
								isti = false;
								break;
							}
						}
					if (!isti) {
                                            // Ovo tu je umjereni parser za umetanje oznaka IDja i poruka
                                            // Trebalo bi dodat nesto na web jer tu vise ne mogu skuzit stzo je poruka
                                            ArrayList<String> temp = new ArrayList<>();
                                            boolean skip = false;
                                            for (int i=0; i<listaNovih.size(); i++) {
                                                String line = listaNovih.get(i);
                                                if ((line.indexOf(": ")>0) && (line.indexOf(": ") == line.indexOf(":")) ){
                                                    temp.add("ID korisnika: "+line.substring(0, line.indexOf(": ")));
                                                    temp.add("Poruka: "+line.substring(line.indexOf(": ")+2));
                                                    skip = true;
                                                } else {
                                                    skip = false;
                                                    temp.add(line);
                                                }
                                            }
                                            rob.write(temp);
				        }
					Scanner sc = new Scanner(Paths.get("newMessage.txt"));
					PrintWriter pw2 = new PrintWriter("oldMessage.txt");
					while (sc.hasNextLine()) {
						pw2.write(sc.nextLine() + System.getProperty("line.separator"));
					}
					sc.close();
					pw2.close();
				} catch (IOException e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null,
							Messages.getString("UrlHandler.errorMsg"));
					t.cancel();
				}
                                
			}
		};
                t.schedule(task, 0l, 999l);
                setGotovo();
	//return true;
        }
        
        

}
