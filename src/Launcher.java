
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Launcher {

    public static double JAVA_VERSION = getJavaVersion();
    public static String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        if (args.length == 0 && JAVA_VERSION > 10) {
            try {
                //Re-launch the app itself with VM option passed
                File currentDir = Paths.get(System.getProperty("user.dir")).toFile();

                if (getLibFolder() == null) {

                    String fileName = "openJFX";

                    if(!new File( fileName + ".zip").exists()) {
                        int dialogButton = JOptionPane.YES_NO_OPTION;
                        int dialogResult = JOptionPane.showConfirmDialog (null, "No OpenJFX installation found. Would you like to download?","OpenJFX", dialogButton);

                        if(dialogResult != JOptionPane.YES_OPTION){
                            JOptionPane.showMessageDialog(null, "Can't run on Java 11 or higher without OpenJFX. Exiting...");
                            System.exit(0);
                            return;
                        }

                        if(!System.getProperty("os.arch").contains("64")) {
                            JOptionPane.showMessageDialog(null, "x86 systems are currently not supported by the auto updater." +
                                    " Please fetch your own version of OpenFX from https://gluonhq.com/products/javafx/");
                            System.exit(0);
                            return;
                        }

                        String downloadPath;
                        if (isWindows()) {
                            downloadPath = "https://github.com/GodDragoner/TeaseAIJava/raw/master/Resources/openjfx_windows-x64_bin-sdk.zip";
                        } else if (isMac()) {
                            downloadPath = "https://github.com/GodDragoner/TeaseAIJava/raw/master/Resources/openjfx_osx-x64_bin-sdk.zip";
                        } else if (isUnix()) {
                            downloadPath = "https://github.com/GodDragoner/TeaseAIJava/raw/master/Resources/openjfx_linux-x64_bin-sdk.zip";
                        } else {
                            JOptionPane.showMessageDialog(null, "Your OS is not supported by JavaFX yet! Exiting.");
                            System.exit(0);
                            return;
                        }


                        URL url = new URL(downloadPath);
                        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                        long completeFileSize = httpConnection.getContentLength();

                        ProgressMonitor progressMonitor = new ProgressMonitor(null, "Downloading OpenJFX...", "", 0, (int) completeFileSize);

                        BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
                        FileOutputStream fos = new FileOutputStream(fileName + ".zip");
                        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

                        byte[] data = new byte[1024];
                        long downloadedFileSize = 0;
                        int x;
                        //int oldProgress = 0;

                        while ((x = in.read(data, 0, 1024)) >= 0) {
                            downloadedFileSize += x;

                            //Calculate progress
                            //final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100d);

                            progressMonitor.setProgress((int) downloadedFileSize);

                            /*if (currentProgress > oldProgress) {
                                TeaseLogger.getLogger().log(Level.INFO, "Download Progress at " + currentProgress + "%");
                                oldProgress = currentProgress;
                            }*/

                            bout.write(data, 0, x);
                        }

                        bout.close();
                        in.close();
                    }


                    //Unzip the downloaded file
                    unzipFile(new File(fileName + ".zip"), currentDir);
                    //Delete the downloaded zip file
                    //newUpdateZipFile.delete();
                }

                Process process = Runtime.getRuntime().exec(new String[]{"java", "--module-path=" + getLibFolder().getPath() + "", "--add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.media,javafx.graphics,javafx.swing,javafx.web", "-jar", "ScriptConverter.jar", "test"});
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }

                input.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            System.exit(0);
        } else {
            Driver.main(args);
        }
    }


    private static File getLibFolder() {
        File currentDir = Paths.get(System.getProperty("user.dir")).toFile();

        File libFolder = null;
        for (File file : currentDir.listFiles()) {
            if (file.isDirectory()) {
                for (File dirFile : file.listFiles()) {
                    if (dirFile.isDirectory() && dirFile.getName().equals("lib")) {
                        libFolder = dirFile;
                    }
                }
            }
        }

        return libFolder;
    }

    public static boolean isWindows() {
        return (OPERATING_SYSTEM.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OPERATING_SYSTEM.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OPERATING_SYSTEM.indexOf("nix") >= 0 || OPERATING_SYSTEM.indexOf("nux") >= 0 || OPERATING_SYSTEM.indexOf("aix") > 0 );
    }

    public static boolean isSolaris() {
        return (OPERATING_SYSTEM.indexOf("sunos") >= 0);
    }
    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }
    
    public static void unzipFile(File inputFile, File outputFolder) {
        try {
            ZipFile zipFile = new ZipFile(inputFile);

            Enumeration zipEntries = zipFile.entries();

            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }

            String outputDir = outputFolder.getAbsolutePath() + File.separator;
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();

                if (zipEntry.isDirectory()) {
                    //System.out.println("Extracting directory: " + outputDir + zipEntry.getName());

                    new File(outputDir + zipEntry.getName()).mkdirs();
                    continue;
                }

                //System.out.println("Extracting file: " + outputDir + zipEntry.getName());

                copyInputStream(zipFile.getInputStream(zipEntry), new BufferedOutputStream(new FileOutputStream(outputDir + zipEntry.getName())));
            }

            zipFile.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    static double getJavaVersion() {
        return Double.parseDouble(System.getProperty("java.specification.version"));
    }
}
