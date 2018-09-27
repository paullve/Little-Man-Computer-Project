public class Instruction {

    public static final String ADD = "Add";
    public static final String SUB = "Subtract";
    public static final String STA = "Store";
    public static final String LDA = "Load";
    public static final String INP = "Input";
    public static final String OUT = "Output";
    public static final String BRA = "Jump To";
    public static final String BRZ = "Jump if Zero";
    public static final String BRP = "Jump if Positive";
    public static final String HLT = "Halt.";

    private String code;
    private String addr;
    private int memAddr;

    private static String program = "Finished Program:\n";

    public Instruction(String code, String addr) {
        this.code = code;
        this.addr = addr;
        memAddr = Integer.parseInt(addr);
    }

    public Instruction() {
        code = "HLT";
        addr = "000";
        memAddr = 0;
    }


    public void execute(){

        switch(code) {

            case ADD:
                Main.lastValue += Integer.parseInt(Main.memory.get(memAddr).getText());
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;
            case SUB:
                Main.lastValue -= Integer.parseInt(Main.memory.get(memAddr).getText());
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;

            case STA:
                Main.memory.get(memAddr).setText(Integer.toString(Main.lastValue));
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;
            case LDA:
                Main.lastValue = Integer.parseInt(Main.memory.get(memAddr).getText());
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;

            case INP:
                Main.lastValue = Main.inputs.poll();
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;

            case OUT:
                Main.outputs.offer(Main.lastValue);
                program += getOpCode(code, addr) + "\n";
                Main.programCounter++;
                break;

            case BRA:
                Main.programCounter = memAddr+1;
                program += getOpCode(code, addr) + "\n";
                break;
            case BRZ:
                if(Main.lastValue == 0)
                    Main.programCounter = memAddr;
                else
                    Main.programCounter++;
                program += getOpCode(code, addr) + "\n";
                break;
            case BRP:
                if(Main.lastValue == 0)
                    Main.programCounter = memAddr;
                else
                    Main.programCounter++;
                program += getOpCode(code, addr) + "\n";
                break;

            case HLT:
                program += getOpCode(code, addr) + "\n";
                break;
            default:
                break;
        }


    }

    public boolean isHalt() { return code.equals(HLT); }
    public boolean isJump() { return code.equals(BRP) || code.equals(BRA) || code.equals(BRZ); }
    public static boolean isJump(String inp) { return inp.equals(BRP) || inp.equals(BRA) || inp.equals(BRZ);}
    public static String getProgram() { return program; }

    static String getOpCode(String in, String addr) {
        String result = "";

        if(in.equals(ADD)) {
            result += "1";
            result += addr;
        }

        else if(in.equals(SUB)) {
            result += "2";
            result += addr;
        }

        else if(in.equals(STA)) {
            result += "3";
            result += addr;
        }

        else if(in.equals(LDA)) {
            result += "5";
            result += addr;
        }

        else if(in.equals(INP)) {
            result += "901";
        }

        else if(in.equals(OUT)) {
            result += "902";
        }

        else if(in.equals(BRA)) {
            result += "6";
            result += addr;
        }

        else if(in.equals(BRZ)) {
            result += "7";
            result += addr;
        }

        else if(in.equals(BRP)) {
            result += 8;
            result += addr;
        }

        else if(in.equals(HLT)) {
            result += "000";
        }




        return result;
    }

    static boolean needsAddress(String in) {

        boolean result = true;

        if(in.equals(INP))
            result = false;
        else if(in.equals(OUT))
            result = false;
        else if(in.equals(HLT))
            result = false;

        return result;
    }


}
