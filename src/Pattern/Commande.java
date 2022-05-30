package Pattern;

public abstract class Commande {
    
    // ====================
    // ===== EXECUTER =====
    // ====================
    public abstract void execute();

    // =======================
    // ===== DESEXECUTER =====
    // =======================
    public abstract void desexecute();
}