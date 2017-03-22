package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileContent {

    public static String[] getCranfieldFileContentSplited(File file) throws IOException {
        String[] contents = new String[5];
        String Title;
        String Abstract;
        String Branch;
        String Content;
        //content[0] = title[.T ], content[1] = abstract[.A ], content[2] =  branch[.B ], content[3] = Words [.W ] 

        String fullcontent = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
        System.out.println(file.getCanonicalPath());
        contents = fullcontent.split(".T", 2);
        contents = contents[1].split(".A", 2);
        Title = contents[0];

        contents = contents[1].split(".B", 2);
        Abstract = contents[0];

        contents = contents[1].split(".W", 2);
        Branch = contents[0];
        Content = contents[1];

        contents = new String[4];

        contents[0] = Title;
        contents[1] = Abstract;
        contents[2] = Branch;
        contents[3] = Content;

        return contents;
    }

    public static String chooseFile(JFrame frame) throws IOException {
        String filepath = "";
        String filename = "";
        File file = null;
        BufferedWriter writer = null;

        JFileChooser fileChooser = new JFileChooser();

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            filepath = fileChooser.getSelectedFile().getPath();
            filename = fileChooser.getSelectedFile().getAbsolutePath();

            int i = 0;
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(".I ", 0)) {

                        file = new File(fileChooser.getSelectedFile().getParent() + "/Doc-" + ++i);
                        if (i > 1) {
                            writer.close();
                        }
                        writer = Files.newBufferedWriter(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
                    }

                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
            }
        }
        return fileChooser.getSelectedFile().getAbsolutePath();
    }

	public static String[] getMedlineFileContentSpilted(File file) {
		// TODO Auto-generated method stub
		return null;
	}



}
