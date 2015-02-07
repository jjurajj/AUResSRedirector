package robot;

import gui.Messages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TheRobot implements ClipboardOwner {

	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	public void write(List<String> l) {
		try {
			Robot robot = new Robot();
			robot.delay(3000); // maknuti na kraju ovo je samo za debug

			// inicijalizacija settingsa
			List<String> settings = Files.readAllLines(
					Paths.get("settings.txt"), Charset.defaultCharset());
			boolean plain = false;
			boolean userid = false;
			boolean message = false;
			if (settings.get(1).equals("Plain"))
				plain = true;
			if (settings.get(2).equals("UserId"))
				userid = true;
			if (settings.get(3).equals("Message"))
				message = true;

			// inicijalizacija configa
			String beforemessage = null;
			String aftermessage = null;
			String beforeid = null;
			String afterid = null;
			String beforeblock = null;
			String afterblock = null;
			boolean beforeline = false;
			boolean afterline = false;
			List<String> config = Files.readAllLines(
					Paths.get("tempConfig.txt"), Charset.defaultCharset());
			if (!plain) {
				for (String i : config) {
					if (i.equals(""))
						continue;
					String[] tokens = i.split(":");
					if (tokens[0].equals("BeforeMessage"))
						beforemessage = tokens[1];
					if (tokens[0].equals("AfterMessage"))
						aftermessage = tokens[1];
					if (tokens[0].equals("BeforeId"))
						beforeid = tokens[1];
					if (tokens[0].equals("AfterId"))
						afterid = tokens[1];
					if (tokens[0].equals("BeforeBlock"))
						beforeblock = tokens[1];
					if (tokens[0].equals("AfterBlock"))
						afterblock = tokens[1];
					if (tokens[0].equals("BeforeLine"))
						beforeline = true;
					if (tokens[0].equals("AfterLine"))
						afterline = true;
				}
			}

			// idem po listi redaka koje trebam ispisati l
			for (int j = 0; j < l.size(); j++) {
				String i = l.get(j);
				// ako pocinje sa ID znaci u novom sam bloku
				if (i.startsWith("ID korisnika: ")) {
					if (beforeblock != null) {
						type(robot, beforeblock);
						robot.delay(400);
					}
					if (beforeid != null) {
						type(robot, beforeid);
						robot.delay(400);
					}
					if (userid) {
						i = i.split(": ")[1];
						i += System.getProperty("line.separator");
						StringSelection stringSelection = new StringSelection(
								i.replace("ID korisnika",
										Messages.getString("IDkorisnika")));
						clipboard.setContents(stringSelection, this);

						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						robot.delay(400);
					}
					if (afterid != null) {
						type(robot, afterid);
						robot.delay(400);
					}
					if (j == l.size() - 1) { // zadnji je pa onda ispisi
						// sekvencu afterblock
						if (afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					} else { // ocito nije zadnji pa se sekvenca afterblock
								// ispisuje jedino ako je sljedeca nova poruka
						String sljed = l.get(j + 1);
						if (sljed.contains("ID korisnika")
								&& afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					}
				} else if (i.startsWith("Poruka: ")) {
					// ako pocinje s Poruka onda sam u prvom retku poruke
					if (beforemessage != null) {
						type(robot, beforemessage);
						robot.delay(400);
					}
					if (message) {
						i = i.split(": ")[1];
						i += System.getProperty("line.separator");
						StringSelection stringSelection = new StringSelection(
								i.replace("Poruka",
										Messages.getString("Poruka")));
						clipboard.setContents(stringSelection, this);

						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						robot.delay(400);
					}
					if (aftermessage != null) {
						type(robot, aftermessage);
						robot.delay(400);
					}
					if (j == l.size() - 1) { // zadnji je pa onda ispisi
						// sekvencu afterblock
						if (afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					} else { // ocito nije zadnji pa se sekvenca afterblock
								// ispisuje jedino ako je sljedeca nova poruka
						String sljed = l.get(j + 1);
						if (sljed.contains("ID korisnika")
								&& afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					}
				} else {
					// inace sam u nekom ne-prvom retku poruke
					if (beforeline) {
						type(robot, beforemessage);
						robot.delay(400);
					}
					if (message) {
						i += System.getProperty("line.separator");
						StringSelection stringSelection = new StringSelection(i);
						clipboard.setContents(stringSelection, this);

						robot.keyPress(KeyEvent.VK_CONTROL);
						robot.keyPress(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_V);
						robot.keyRelease(KeyEvent.VK_CONTROL);
						robot.delay(400);
					}
					if (afterline) {
						type(robot, aftermessage);
						robot.delay(400);
					}
					if (j == l.size() - 1) { // zadnji je pa onda ispisi
												// sekvencu afterblock
						if (afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					} else { // ocito nije zadnji pa se sekvenca afterblock
								// ispisuje jedino ako je sljedeca nova poruka
						String sljed = l.get(j + 1);
						if (sljed.contains("ID korisnika")
								&& afterblock != null) {
							type(robot, afterblock);
							robot.delay(400);
						}
					}
				}
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void type(Robot robot, String s) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		boolean relctrl = false;
		boolean relalt = false;
		boolean relshift = false;
		String[] keys = s.split("\\+");
		List l = new ArrayList();
		for (String j : keys) {
			if (j.startsWith("<") && j.endsWith(">")) {
				j = j.substring(1, j.length() - 1);
				Class<?> c = KeyEvent.class;
				Field f = c.getDeclaredField("VK_" + j);
				robot.keyPress(f.getInt(f));
				l.add(f.getInt(f));
				if (j.equals("CONTROL"))
					relctrl = true;
				if (j.equals("ALT"))
					relalt = true;
				if (j.equals("SHIFT"))
					relshift = true;
			} else {
				if (relctrl)
					robot.keyRelease(KeyEvent.VK_CONTROL);
				if (relalt)
					robot.keyRelease(KeyEvent.VK_ALT);
				if (relshift)
					robot.keyRelease(KeyEvent.VK_SHIFT);
				StringSelection stringSelection = new StringSelection(j);
				robot.delay(400);
				clipboard.setContents(stringSelection, this);

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
			}
		}
		for (int i = l.size() - 1; i >= 0; i--) {
			robot.keyRelease((int) l.get(i));
		}
	}

	@Override
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
	}

}
