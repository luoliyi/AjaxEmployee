package nf.luoliyi.vo;

public class Employee {

    private  int eid;
    private String ename;
    private int eage;
    private int esex;
    private String eemail;
    private String edesc;

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getEage() {
        return eage;
    }

    public void setEage(int eage) {
        this.eage = eage;
    }

    public int getEsex() {
        return esex;
    }

    public void setEsex(int esex) {
        this.esex = esex;
    }

    public String getEemail() {
        return eemail;
    }

    public void setEemail(String eemail) {
        this.eemail = eemail;
    }

    public String getEdesc() {
        return edesc;
    }

    public void setEdesc(String edesc) {
        this.edesc = edesc;
    }


    public Employee(){

    }

    public  Employee(String ename, int eage, int esex, String eemail, String edesc){

        this.ename = ename;
        this.eage = eage;
        this.esex = esex;
        this.eemail = eemail;
        this.edesc = edesc;
    }
    public Employee(int eid, String ename, int eage, int esex, String eemail, String edesc) {
        this.eid = eid;
        this.ename = ename;
        this.eage = eage;
        this.esex = esex;
        this.eemail = eemail;
        this.edesc = edesc;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "eid=" + eid +
                ", ename='" + ename + '\'' +
                ", eage=" + eage +
                ", esex=" + esex +
                ", eemail='" + eemail + '\'' +
                ", edesc='" + edesc + '\'' +
                '}';
    }
}
