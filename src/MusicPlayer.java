import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/*
 * Created on 26 juil. 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Alexis Kinsella
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MusicPlayer
{
	private static boolean isPlaying = false;
	private static StoppableThread st;
	private static Shell shell;
	private static Display display;
	private static Button btnPlay;
	private static org.eclipse.swt.widgets.List l;
	private static File[] fileLists;
	private static Button btnNext;
	private static Button btnPrevious;
	private static Image play;
	private static Image stop;
	private static Image previous;
	private static Image next;
	private static Image app;

	public static void centerOnFirstScreen(Shell shell)
	{
		Point shellSize = shell.getSize();
		Rectangle monitorBounds = shell.getDisplay().getMonitors()[0].getBounds();
		shell.setLocation(monitorBounds.x + (monitorBounds.width - shellSize.x) / 2, monitorBounds.y + (monitorBounds.height - shellSize.y) / 2);
	}

	public static void main(String[] args)
		{
			try
			{
				display = new Display();
				play = new Image(display, MusicPlayer.class.getResourceAsStream("/res/icon/media_play_green.png"));
				stop = new Image(display, MusicPlayer.class.getResourceAsStream("/res/icon/media_stop.png"));
				next = new Image(display, MusicPlayer.class.getResourceAsStream("/res/icon/media_fast_forward.png"));
				previous = new Image(display, MusicPlayer.class.getResourceAsStream("/res/icon/media_rewind.png"));
				app = new Image(display, MusicPlayer.class.getResourceAsStream("/res/icon/music.ico"));
				shell = new Shell();
				shell.setText("MusicPlayer");
				shell.setImage(app);
				shell.setBounds(0, 0, 500, 600);
				Menu mb = new Menu(shell, SWT.BAR);
				MenuItem fileMI = new MenuItem(mb, SWT.CASCADE);
				fileMI.setText("Fichiers");
				Menu fileM = new Menu(fileMI);
				fileMI.setMenu(fileM);
				MenuItem aboutMI = new MenuItem(fileM, SWT.PUSH);
				aboutMI.setText("A propos");
				aboutMI.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{
						MessageBox mb = new MessageBox(shell);
						mb.setText("A propos");
						mb.setMessage("Auteur: Alexis Kinsella\n" +
								"Mail: alexiskinsella@free.fr\n" +
								"Date: 27/07/2005");
						mb.open();
					}
				});
				new MenuItem(fileM, SWT.SEPARATOR);
				MenuItem quitMI = new MenuItem(fileM, SWT.PUSH);
				quitMI.setText("Quitter");
				quitMI.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{
						stop();
						shell.dispose();
					}
				});
				shell.setMenuBar(mb);
				centerOnFirstScreen(shell);
				shell.setLayout(LayoutUtil.createGridLayout(3, 5, 5, 5, 5, false));
				l = WidgetUtil.createList(shell, LayoutUtil.createGridData(SWT.FILL, SWT.FILL, SWT.DEFAULT, SWT.DEFAULT, 3, SWT.DEFAULT), SWT.FLAT | SWT.BORDER | SWT.V_SCROLL);
				btnPlay = WidgetUtil.createButton(shell, "Play", LayoutUtil.createGridData(SWT.FILL, SWT.DEFAULT), SWT.PUSH);
				btnPlay.setImage(play);
				btnPrevious = WidgetUtil.createButton(shell, "<<", LayoutUtil.createGridData(SWT.FILL, SWT.DEFAULT), SWT.PUSH);
				btnPrevious.setImage(previous);
				btnNext = WidgetUtil.createButton(shell, ">>", LayoutUtil.createGridData(SWT.FILL, SWT.DEFAULT), SWT.PUSH);
				btnNext.setImage(next);
				File conf = new File("conf");
				conf.mkdirs();
				Properties props = new Properties();
				File configFile = new File(conf.getAbsolutePath() + File.separatorChar + "MusicPlayer.conf");
				String lastOpenedFolder = null;
				if (configFile.exists()) 
				{
					if (!configFile.isFile()) throw new Exception("Effacez le dossier conf, il y a un problème avec! Relancez ensuite l'application");
					try
					{
						props.load(new FileInputStream(configFile));
						if (props.containsKey("lastOpenedFolder")) lastOpenedFolder = (String)props.get("lastOpenedFolder");
					}
					catch (Exception e) { e.printStackTrace(); }
				}
				DirectoryDialog dd = new DirectoryDialog(shell, SWT.SINGLE);
				if (lastOpenedFolder != null && new File(lastOpenedFolder).exists()) dd.setFilterPath(lastOpenedFolder);
				String result = dd.open();
				if (result == null)
				{
					shell.dispose();
					display.dispose();
					return;
				}
				props.setProperty("lastOpenedFolder", result);
				props.store(new FileOutputStream(configFile), new Date().toString());
				shell.addListener(SWT.Dispose, new Listener()		
				{
					public void handleEvent(Event event)
					{
						if (play != null) play.dispose();
						if (stop != null) stop.dispose();
						if (next != null) next.dispose();
						if (previous != null) previous.dispose();
						if (app != null) app.dispose();
					}
				});
				fileLists = new File(result).listFiles(new FileFilter()
				{
					public boolean accept(File pathname) { return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".mp3"); }
				});
				btnPlay.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{
						isPlaying = !isPlaying;
						btnPlay.setImage(isPlaying ? stop : play);
						if (fileLists.length <= 0) return;
						if (!isPlaying) stop();
						else play(fileLists[l.getSelectionIndex()]);
					}
				});
				btnPrevious.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{
						isPlaying = true;
						btnPlay.setImage(stop);					
						previous();
					}
				});
				btnNext.addListener(SWT.Selection, new Listener()
				{
					public void handleEvent(Event event)
					{
						isPlaying = true;
						btnPlay.setImage(stop);					
						next();
					}
				});
				l.addListener(SWT.DefaultSelection, new Listener()
				{
					public void handleEvent(Event event)
					{
						stop();
						isPlaying = true;
						btnPlay.setImage(stop);					
						play(fileLists[l.getSelectionIndex()]);
						l.setTopIndex(l.getSelectionIndex());
					}
				});
				shell.addListener(SWT.Close, new Listener()
				{
					public void handleEvent(Event event) { stop(); }
				});
				
				List l2 = new ArrayList();
				for (int i = 0 ; i < fileLists.length ; i++)
				{
					l2.add(fileLists[i]);
				}
				List l1 = new ArrayList();
				int length = l2.size();
				while(length > 0)
				{
					l1.add(l2.remove((int)(Math.random() * length)));
					length--;
				}
				fileLists = (File[])l1.toArray(fileLists);
				Iterator it = l1.iterator();
				while(it.hasNext())
				{
					File file1 = (File)it.next();
					l.add(file1.getName());
				}
				l.select(0);
				shell.open();
				while(!shell.isDisposed())
				{
					if(!display.readAndDispatch()) display.sleep();
				}	
			}
			catch(Exception e) 
			{
				if (shell != null) shell.dispose();
				if (display != null) display.dispose(); 
				System.exit(1); 
			}
		}

		/**
		 * @param file
		 */
		protected static void play(final File file)
		{
			shell.setText("Music player - " + file.getName());
			st = new StoppableThread()
			{
				public void run()
				{
					AudioInputStream din = null;
					try
					{
						AudioInputStream in = AudioSystem.getAudioInputStream(file);
						AudioFormat baseFormat = in.getFormat();
						AudioFormat decodedFormat = new AudioFormat(
								AudioFormat.Encoding.PCM_SIGNED,
								baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
								baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
								false);
						din = AudioSystem.getAudioInputStream(decodedFormat, in);
						DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
						SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
						if(line != null)
						{
							line.open(decodedFormat);
							byte[] data = new byte[16384];
							// Start
							line.start();
							System.out.println(file.getName() +  " (" + file.getAbsolutePath() + ")");
							int nBytesRead;
							while ((nBytesRead = din.read(data, 0, data.length)) != -1)
							{
								if (shouldStop()) break;
								line.write(data, 0, nBytesRead);
							}
							// Stop
							line.drain();
							line.stop();
							line.close();
							din.close();
						}
					}
					catch(Exception e) { e.printStackTrace(); }
					finally
					{
						if(din != null) { try { din.close(); } catch(IOException e) { } }
					}
					din = null;
					if (!shouldStop()) next();
				}
			};
			st.setPriority(Thread.MAX_PRIORITY);
			st.start();
		}
		
		private static void previous()
		{
			display.asyncExec(
				new Runnable()
				{
					public void run()
					{
						stop();
						isPlaying = true;
						btnPlay.setImage(stop);					
						int size = l.getItemCount();
						if (l.getSelectionIndex() - 1 < 0) 
						{
							l.select(size - 1);
							l.setTopIndex(l.getSelectionIndex());
							play(fileLists[size - 1]);
						}
						else
						{
							l.select(l.getSelectionIndex() - 1);
							l.setTopIndex(l.getSelectionIndex());
							play(fileLists[l.getSelectionIndex()]);
						}
					}
				}
			);
		}
		
		private static void next()
		{
			display.asyncExec(
				new Runnable()
				{
					public void run()
					{
						stop();
						isPlaying = true;
						btnPlay.setImage(stop);					
						int size = l.getItemCount();
						if (l.getSelectionIndex() + 1 >= size) 
						{
							l.select(0);
							l.setTopIndex(l.getSelectionIndex());
							play(fileLists[0]);
						}
						else
						{
							l.select(l.getSelectionIndex() + 1);
							l.setTopIndex(l.getSelectionIndex());
							play(fileLists[l.getSelectionIndex()]);
						}
					}
				}
			);
		}

		/**
		 * 
		 */
		protected static void stop()
		{
			if (st != null && st.isAlive()) st.stopThread();
		}
}
