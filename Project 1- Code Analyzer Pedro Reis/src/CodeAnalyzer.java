import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeAnalyzer {
    public static String inputPath = null;

    // args[0] is expected to be "-i"
    // args[1] is expected to be the path
    public static void main(String[] args) {
        validateInput(args);
        inputPath = args[1];
        for (File file : new File(inputPath).listFiles()) { // iterates through the list of files found in the directory
            process(file.getName()); // displays if this file directly or indirectly includes any other file multiple times
        }
    }

    // prints an error message and throws an exception if args are invalid
    public static void validateInput(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java DirectoryValidator -i <directory_path>");
            throw new IllegalArgumentException();
        }

        // Validate first argument
        if (!"-i".equals(args[0])) {
            System.err.println("First argument must be '-i'");
            throw new IllegalArgumentException();
        }

        // Validate second argument as a directory
        File directory = new File(args[1]);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("The provided path is not a valid existing directory");
            throw new IllegalArgumentException();
        }
    }

    private static void process(String filename) {
        // the different ways to reach a header are stored in `fileToDependencyPathMapping`
        // key: a header file
        // value: list of values that header file is accessed in the dependency tree
        Map<String, LinkedList<String>> fileToDependencyPathMapping = new Map<>();

        recurse(filename, filename, fileToDependencyPathMapping); // traverse the dependency tree and populates `fileToDependencyPathMapping`

        for (Map<String, LinkedList<String>>.Entry entry : fileToDependencyPathMapping.entries()) {
            if (entry.value.size() >= 2) { // checks if a file has been accessed multiple times
                System.out.format("%s is included %d times in %s%n", entry.key, entry.value.size(), filename); // prints the number of accesses

                int index = 1;
                for (String dependencyPath : entry.value) {
                    System.out.println("   " + index + ". " + dependencyPath); // list down the path of access in the dependency tree
                    index++;
                }
                System.out.println();
            }
        }
    }

    // traverses through the dependency tree recursively and find the different ways to reach header files
    // the different ways found are stored in `fileToDependencyPathMapping`
    private static void recurse(String filename, String dependencyPath, Map<String, LinkedList<String>> fileToDependencyPathMapping) {
        fileToDependencyPathMapping.putIfAbsent(filename, new LinkedList<>()); // add an empty list as default value

        // adds the dependency path to the list of dependency paths, through which the file `filename` can be reached
        fileToDependencyPathMapping.get(filename).add(dependencyPath);

        for (String i : getHeaders(filename)) { // iterates through the headers found in the file given by `filename`
            recurse(i, dependencyPath + " --> " + i, fileToDependencyPathMapping); // recursively calls the method on the headers
        }
    }

    // gets the included headers found in this the given file as a list of Strings
    private static LinkedList<String> getHeaders(String filename) {
        LinkedList<String> headers = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File(inputPath + "/" + filename))) {
            // regex to match includes
            Pattern pattern = Pattern.compile("#include\\s*\"([^\"]+)\"");

            while (scanner.hasNextLine()) { // iterates through the file
                Matcher matcher = pattern.matcher(scanner.nextLine());
                if (matcher.find()) headers.add(matcher.group(1)); // adds header if found in the current line
            }
        } catch (FileNotFoundException e) {
            // file not found
        }
        return headers;
    }
}
