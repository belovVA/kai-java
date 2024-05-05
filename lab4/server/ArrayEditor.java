package lab4.server;

public class ArrayEditor {
    private final int[][] intArray = new int[10][10];
    private final float[][] floatArray = new float[10][10];
    private final String[][] strArray = new String[10][10];
    public ArrayEditor() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                intArray[i][j] = i + j;
                floatArray[i][j] = Float.parseFloat(String.valueOf(i + j));
                strArray[i][j] = String.valueOf((char)(i + j + 65));
            }
        }
        System.out.println("Массив инициализирован");
    }


    public void setInt(int i, int j, int value) {
        intArray[i][j] = value;
    }

    public void setFloat(int i, int j, float value) {
        floatArray[i][j] = value;
    }

    public void setString(int i, int j, String value) {
        strArray[i][j] = value;
    }

    public int getInt (int i, int j) {
        return intArray[i][j];
    }

    public float getFloat (int i, int j) {
        return floatArray[i][j];
    }

    public String getString (int i, int j) {
        return strArray[i][j];
    }

    public int[][] getIntArray() {
        return intArray;
    }

    public float[][] getFloatArray() {
        return floatArray;
    }

    public String[][] getStrArray() {
        return strArray;
    }

    public int[] getLength(String arr) {
        int[] len = new int[2];
        switch (arr) {
            case ("int"):
                len[0] = intArray.length;
                len[1] = intArray[0].length;
                break;
            case ("float"):
                len[0] = floatArray.length;
                len[1] = floatArray[0].length;
                break;
            case ("string"):
                len[0] = strArray.length;
                len[1] = strArray[0].length;
                break;
        }
        return len;
    }
}
