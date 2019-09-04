import java.io.PrintWriter;
import java.lang.StringBuilder;

//printing to python
public class pprinter{

    public static void printData(String[] data, String[] labels, String fileName){
        fileName += ".txt";
        String dataString = data2string(data, labels);
        try {
            PrintWriter OutputStream = new PrintWriter(fileName);
            OutputStream.println(dataString);
            OutputStream.close();
        }
        catch (Exception e){
        }

    }

    public static void printData(String[] data, int[] labels, String fileName){
        fileName += ".txt";
        String dataString = data2string(data, labels);
        try {
            PrintWriter OutputStream = new PrintWriter(fileName);
            OutputStream.println(dataString);
            OutputStream.close();
        }
        catch (Exception e){
        }

    }

    private static String data2string(String[] data, String[] labels){
        StringBuilder dictBuild = new StringBuilder();

        dictBuild.append("{");
        for (int i = 0; i<data.length; i++){
            dictBuild.append("\"");
            dictBuild.append(labels[i]);
            dictBuild.append("\":");
            dictBuild.append(data[i]);
            dictBuild.append(",");
        }
        dictBuild.deleteCharAt(dictBuild.length()-1);
        dictBuild.append("}");
        return dictBuild.toString();
    }

    private static String data2string(String[] data, int[] labels){
        StringBuilder dictBuild = new StringBuilder();

        dictBuild.append("{");
        for (int i = 0; i<data.length; i++){
            dictBuild.append(String.valueOf(labels[i]) + ":" + data[i] +",");
        }
        dictBuild.deleteCharAt(dictBuild.length()-1);
        dictBuild.append("}");
        return dictBuild.toString();
    }
    
    public static String array2string(double[] array){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (double elem: array){
            builder.append(String.valueOf(elem) +",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

    public static String array2string(String[] array){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String elem: array){
            builder.append("\""+elem +"\",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

    public static String printStringArray(String[] array){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String elem: array){
            builder.append(elem + ",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

   
    public static String array2string(int[] array){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (double elem: array){
            builder.append(String.valueOf(elem)+",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

    //For Potential sketching
    public static String array2string(double[][] array){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (double[] subarray : array){
            builder.append("[");
            for (double elem: subarray){
                builder.append(String.valueOf(elem));
                builder.append(",");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("],");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        return builder.toString();
    }

    //For animation frames
    public static String array2string(double[][][] array){
        String[] subStrings = new String[array.length];
        for (int it = 0; it<array.length; it++){
            subStrings[it] = array2string(array[it]);            
        }
        return printStringArray(subStrings);
    }

}
