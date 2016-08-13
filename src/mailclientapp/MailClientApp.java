package mailclientapp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import mailprotocol.MailProtocol;
import org.jdom2.output.Format;  
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;  
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


/**
 *
 * @author ethenq
 */
public class MailClientApp extends javax.swing.JFrame {
    
    private static final long serialVersionUID = 1L;
    
    ////////////////////
    // obiekty i flagi
    ////////////////////
    
    // informacje
    private int port;
    private static String user = "";
    
    // gniazdo klienta
    private Socket clientSocket;
    
    // obiekt naszego protokołu mailowego
    private static MailProtocol mailProtocol;
    
    // obiekt okna logowania
    private static LoginDialog loginDialog;
    private InfoDialog /*NewJDialog*/ infoDialog;
    private SettingsDialog settingsDialog;
    
    // bufory wejścia i wyjścia    
    private static BufferedInputStream reader;
    private static ObjectInputStream obReader;
    
    private static BufferedOutputStream writer;
    private static ObjectOutputStream obWriter;
    
    // obiekt ramki wiadomości mail
    private static Map<String, Object> mail_frame = new HashMap<>();
    
    // obiekty list maili itp.
    private List<Object> list = new ArrayList<>();                  // lista obiektów
    
    private DefaultListModel recModel = new DefaultListModel();        // zbiór rekordów w graficznej liście maili
    private DefaultListModel sentModel = new DefaultListModel();
    private DefaultListModel trashModel = new DefaultListModel();
    
    ListSelectionModel listSelectionModel;                          // rodzaj modelu powyższego
    ListSelectionModel sentListSelectionModel;
    ListSelectionModel trashListSelectionModel;
    
    boolean notConnected;
    private String m_listType;
    
    /**
     * Creates new form MailClientUI
     */
    public MailClientApp() { 
        setTitle("Klient poczty E-mail");
        
        // tworzę obiekt biblioteki z implementacją mojego protokołu aplikacji
        mailProtocol = new MailProtocol();
        
        list.add(0, null);
        list.add(1, null);

        initComponents();
        m_listType = "rec";
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        
        Dimension windowSize = getSize();
        int x = (int)windowSize.getWidth();
        int y = (int)windowSize.getHeight();
        
        setLocation((w/2)-(x/2), (h/2)-(y/2));
        setResizable(false);
    }

    
    // KLASA DO LISTY MAILI:
    class MyCellRenderer extends JPanel implements ListCellRenderer { 
        private static final long serialVersionUID = 1L;
        JCheckBox checkbox;
        JLabel left, middle, right;
        JSeparator sep_1, sep_2;

        MyCellRenderer() {
            GridBagLayout grid = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            setLayout(grid);
            
            checkbox = new JCheckBox();
            left = new JLabel();
            middle = new JLabel();
            right = new JLabel();
            
            sep_1 = new JSeparator();
            sep_2 = new JSeparator();
            sep_1.setOrientation(SwingConstants.VERTICAL);
            sep_2.setOrientation(SwingConstants.VERTICAL);
            
            //left.setOpaque(true);
            //middle.setOpaque(true);
            //right.setOpaque(true);
            
            Dimension cb_min = new Dimension();
            cb_min.setSize(100 /*420*/ /*295*/, 20);       //320
            
            Dimension cb_max = new Dimension();
            cb_max.setSize(100 /*420*/ /*295*/, 20);
            
            
            
            Dimension l_min = new Dimension();
            l_min.setSize(600 /*420*/ /*295*/, 20);       //320
            
            Dimension l_max = new Dimension();
            l_max.setSize(600 /*420*/ /*295*/, 20);
            
            //Dimension m_min = new Dimension();
            //m_min.setSize(50 /*295*/, 20);
            
            //Dimension m_max = new Dimension();
            //m_max.setSize(50 /*295*/, 20);
            
            //Dimension r_min = new Dimension();
            //r_min.setSize(5, 20);
            
            //Dimension r_max = new Dimension();
            //r_max.setSize(5, 20);
            
            
            checkbox.setMinimumSize(cb_min);
            checkbox.setMaximumSize(cb_max);
            
            
            left.setMinimumSize(l_min);
            left.setMaximumSize(l_max);
            
            //middle.setMinimumSize(m_min);
            //middle.setMaximumSize(m_max);
            
            //right.setMinimumSize(r_min);
            //right.setMaximumSize(r_max);
            //right.setLocation(400, 0);
            //right.setBounds(50, 0, 100, 20);
            //right.setHorizontalAlignment(SwingConstants.LEFT);
            
            middle.setHorizontalAlignment(SwingConstants.RIGHT);
            // middle.setHorizontalTextPosition(SwingConstants.CENTER);
            
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            
            //c.insets = new Insets(2,2,2,2);
            c.anchor = GridBagConstraints.LINE_START /*NORTHEAST*/;
            c.fill = GridBagConstraints./*NONE*/BOTH;
            
            //c.anchor = GridBagConstraints.LINE_START /*RELATIVE*//*NONE*/; // GridBagConstraints.WEST;
            //c.gridheight = 1; //1000;
            //c.gridwidth = 1; //2;
            //c.ipadx = 220; //220;
            
            //c.insets = new Insets(0, 0, 0, 100);
            
            //c.gridwidth = 60;
            add(checkbox, c);
            
            c.insets = new Insets(0, 272, 0, 20);
            add(sep_1, c);
            c.insets = new Insets(0, 90, 0, 20);
            add(sep_2, c);
            
            c.insets = new Insets(0, 22, 0, 140);
            
            c.ipadx = 0;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.ipadx = 20 /*200*/;
            
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 400;
            add(left, c);
            
            c.anchor = GridBagConstraints.BASELINE;
            c.fill = GridBagConstraints.RELATIVE;
            c.insets = new Insets(0, 80, 0, 0);
            
            c.ipadx = 0;
            c.gridx = 1;
            c.gridy = 0;
            //c.weightx = 150;
            add(middle, c);
            
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 0, 0, 0);
            
            c.ipadx = 0;
            c.gridx = 2;
            c.gridy = 0;
            add(right, c);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String leftData = ((String[])value)[0];
            String rightData = ((String[])value)[1];
            String middleData = ((String[])value)[3];
            
            left.setText(leftData);
            middle.setText(middleData);
            right.setText(rightData);
            if(isSelected){
                checkbox.setBackground(list.getSelectionBackground());
                checkbox.setForeground(list.getSelectionForeground());
                left.setBackground(list.getSelectionBackground());
                left.setForeground(list.getSelectionForeground());
                middle.setBackground(list.getSelectionBackground());
                middle.setForeground(list.getSelectionForeground());
                right.setBackground(list.getSelectionBackground());
                right.setForeground(list.getSelectionForeground());
            }
            else{
                checkbox.setBackground(list.getBackground());
                checkbox.setForeground(list.getForeground());
                left.setBackground(list.getBackground());
                left.setForeground(list.getForeground());
                middle.setBackground(list.getBackground());
                middle.setForeground(list.getForeground());
                right.setBackground(list.getBackground());
                right.setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButtonWriteMail = new javax.swing.JButton();
        jLabelWelcome = new javax.swing.JLabel();
        jButtonRecMails = new javax.swing.JButton();
        jButtonTrashMails = new javax.swing.JButton();
        jButtonSendMails = new javax.swing.JButton();
        jButtonLogout = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListMails = new javax.swing.JList();
        jLabelMsgTitle = new javax.swing.JLabel();
        jLabelMsgDate = new javax.swing.JLabel();
        jLabelMsgSender = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuWriteMail = new javax.swing.JMenu();
        jMenuItemRec = new javax.swing.JMenuItem();
        jMenuItemSentMails = new javax.swing.JMenuItem();
        jMenuItemTrash = new javax.swing.JMenuItem();
        jMenuItemWriteMail = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSettings = new javax.swing.JMenuItem();
        jMenuItemInfo = new javax.swing.JMenuItem();
        jMenuItemLogout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonWriteMail.setText("Napisz E-mail");
        jButtonWriteMail.setToolTipText("Otwiera ekran pisania wiadomości e-mail");
        jButtonWriteMail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonWriteMailMouseClicked(evt);
            }
        });
        jButtonWriteMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWriteMailActionPerformed(evt);
            }
        });

        jButtonRecMails.setText("Odebrane wiadomości");
        jButtonRecMails.setToolTipText("Lista odebranych przez użytkownika wiadomości");
        jButtonRecMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecMailsActionPerformed(evt);
            }
        });

        jButtonTrashMails.setText("Kosz");
        jButtonTrashMails.setToolTipText("Lista wyrzuconych do kosza wiadomości");
        jButtonTrashMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTrashMailsActionPerformed(evt);
            }
        });

        jButtonSendMails.setText("Wysłane wiadomości");
        jButtonSendMails.setToolTipText("Lista wysłanych przez użytkownika wiadomości");
        jButtonSendMails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendMailsActionPerformed(evt);
            }
        });

        jButtonLogout.setText("Wyloguj");
        jButtonLogout.setToolTipText("Kliknij aby się wylogować z konta");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonWriteMail, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(jLabelWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonRecMails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTrashMails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSendMails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonWriteMail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonRecMails, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSendMails, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonTrashMails, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jButtonLogout)
                .addContainerGap())
        );

        jListMails.setModel(recModel);
        jListMails.setCellRenderer(new MyCellRenderer());
        jScrollPane1.setViewportView(jListMails);
        //jListMails.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        jListMails.setFixedCellWidth(445);

        listSelectionModel = jListMails.getSelectionModel();
        listSelectionModel.addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                    int firstIndex = e.getFirstIndex();
                    int lastIndex = e.getLastIndex();
                    boolean isAdjusting = e.getValueIsAdjusting();
                    System.out.println("Event for indexes "
                        + firstIndex + " - " + lastIndex
                        + "; isAdjusting is " + isAdjusting
                        + "; selected indexes:");

                    if (lsm.isSelectionEmpty()) {
                        System.out.println(" <none>");
                    } else {
                        // Find out which indexes are selected.
                        int minIndex = lsm.getMinSelectionIndex();
                        int maxIndex = lsm.getMaxSelectionIndex();
                        for (int i = minIndex; i <= maxIndex; i++) {
                            if (lsm.isSelectedIndex(i)) {
                                lsm.setAnchorSelectionIndex(i);

                                System.out.println(" " + i);

                                String x = ( (String[])jListMails.getSelectedValue() )[2];
                                System.out.println("Component name: " + x);

                                // jListMails.clearSelection();

                                JFrame jframe = (JFrame) SwingUtilities.getRoot(jListMails);

                                final MailReadDialog readMailDialog;
                                try {
                                    readMailDialog = new MailReadDialog(jframe, true, i, x, user, m_listType);

                                    readMailDialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosed(WindowEvent e) {
                                            /*String*/ int name = readMailDialog.getDeletedMailName();

                                            if (name != -1 /*!name.equals("")*/)
                                            {
                                                System.out.println("Usuwanie z listy odebranych maila: " + name);

                                                String temp[] = (String[]) recModel.elementAt(name);
                                                System.out.println("Folder do usunięcia: " + temp[2]);

                                                recModel.removeElementAt(name);  /*jListMails.getSelectedIndex()*/ /*name*/

                                                //removeMail(temp[2]);
                                            }

                                            //jListMails.clearSelection();
                                        }
                                    });

                                    readMailDialog.setVisible(true);
                                }
                                catch (FileNotFoundException ev) {
                                }

                            }
                        }
                    }
                    System.out.println("");
                }
            });

            jLabelMsgTitle.setText("Tytuł Wiadomości");

            jLabelMsgDate.setText("Data Odebrania");

            jLabelMsgSender.setText("Nadawca");

            jButton1.setText("Dodaj do kosza");

            jButton2.setText("Usuń wiadomości");

            jButton3.setText("Oznacz jako przeczytane");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(jButton1)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton2)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton3)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(28, 28, 28)
                            .addComponent(jLabelMsgTitle)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelMsgSender)
                            .addGap(71, 71, 71)
                            .addComponent(jLabelMsgDate)
                            .addGap(22, 22, 22))))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelMsgTitle)
                                .addComponent(jLabelMsgDate)
                                .addComponent(jLabelMsgSender))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton1)
                                .addComponent(jButton2)
                                .addComponent(jButton3))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );

            jMenuWriteMail.setText("Menu");
            jMenuWriteMail.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuWriteMailActionPerformed(evt);
                }
            });

            jMenuItemRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemRec.setText("Odebrane wiadomości");
            jMenuItemRec.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemRecActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemRec);

            jMenuItemSentMails.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSentMails.setText("Wysłane wiadomości");
            jMenuItemSentMails.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSentMailsActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemSentMails);

            jMenuItemTrash.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemTrash.setText("Kosz");
            jMenuItemTrash.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemTrashActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemTrash);

            jMenuItemWriteMail.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemWriteMail.setText("Napisz e-mail");
            jMenuItemWriteMail.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemWriteMailActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemWriteMail);
            jMenuWriteMail.add(jSeparator1);

            jMenuItemSettings.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSettings.setText("Ustawienia programu");
            jMenuItemSettings.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSettingsActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemSettings);

            jMenuItemInfo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemInfo.setText("Informacje");
            jMenuItemInfo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemInfoActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemInfo);

            jMenuItemLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemLogout.setText("Wyloguj");
            jMenuItemLogout.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemLogoutActionPerformed(evt);
                }
            });
            jMenuWriteMail.add(jMenuItemLogout);

            jMenuBar1.add(jMenuWriteMail);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 10, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void removeMail(String name)
    {
        String path = "database/rec_mails/" + user + "/" + name;
        System.out.println("Mail to remove: " + path);
        
        // usuwanie plików i folderu z mailem
        File dir = new File(path);
        String files[] = dir.list();
        
        for (String one : files)
        {
            File file = new File("database/rec_mails/" + user + "/" + name + "/" + one);
            
            System.out.println("One: " + one);
            file.delete();
        }
        dir.delete();
    }
    
    void writeMail()
    {
        final MailWriteDialog writeMail = new MailWriteDialog(this, true, user);
        
        writeMail.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {

                if (writeMail.getReturnStatus() == 0)
                {
                    System.out.println("Zamknięcie okna pisania wiadomości.");
                }
                else {
                    //// ZŁOŻENIE RAMKI WIADOMOŚCI DO WYSŁANIA DO KLIENTA DOCELOWEGO
                    
                    Map<String, Object> data;
                    data = writeMail.getMailData();
                    data.put("sender", user);
                    Date dNow = new Date( );
                    SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
                    data.put("datetime", ft.format(dNow));
                    try {
                        mail_frame = mailProtocol.generateMail(data);
                    } catch (IOException ex) {
                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String comm = mailProtocol.getCommand("request", "send mail");
                    try {
                        list.set(0, comm);
                        list.set(1, null);
                        
                        System.out.println("Lista: element 0: " + list.get(0));
                        System.out.println("Lista: element 1: " + list.get(1));
                        
                        sendCommand();
                    } catch (IOException ex) {
                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //// UTWORZENIE PLIKU Z MAILEM W KATALOGU WYSŁANYCH
                    saveMailXML("sent", data);
                    
                    //// DODANIE DO LISTY GUI WYSŁANYCH MAILI
                    
                    String tmp_table[] = new String[4];
                    
                    tmp_table[0] = data.get("title").toString();
                    tmp_table[1] = data.get("datetime").toString();
                    tmp_table[2] = data.get("title").toString();
                    tmp_table[3] = data.get("receiver").toString();
                    
                    sentModel.add(0, tmp_table);
                    
                    //addMailToSentList(data.get("title").toString());
                }
            }
        });
        
        writeMail.setVisible(true);
    }
    
    
    private void jButtonWriteMailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonWriteMailMouseClicked
        // TODO add your handling code here:
        writeMail();
    }//GEN-LAST:event_jButtonWriteMailMouseClicked

    private void jMenuItemLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogoutActionPerformed
        try {
            // wysłanie rozkazu "BYE" do serwera
            list.set(0, "BYE");
            list.set(1, null);
            
            sendCommand();
        } catch (IOException ex) {
            Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItemLogoutActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        jMenuItemLogoutActionPerformed(evt);
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void jMenuItemInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInfoActionPerformed
        if (infoDialog == null)
        {
            infoDialog = new /*NewJDialog*/ InfoDialog(new JFrame(), true);
        
            System.out.println("Obiekt InfoDialog utworzony. Event zadziałał.");
        }
        infoDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItemInfoActionPerformed

    private void jButtonWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWriteMailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonWriteMailActionPerformed

    private void jMenuWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuWriteMailActionPerformed

    }//GEN-LAST:event_jMenuWriteMailActionPerformed

    private void jMenuItemWriteMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWriteMailActionPerformed
        writeMail();
    }//GEN-LAST:event_jMenuItemWriteMailActionPerformed

    private void jMenuItemSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSettingsActionPerformed
        if (settingsDialog == null)
        {
            settingsDialog = new SettingsDialog(new JFrame(), true);
            
            System.out.println("Obiekt SettingsDialog utworzony.");
        }
        settingsDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItemSettingsActionPerformed

    private void jButtonRecMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecMailsActionPerformed
        showList("received_mails");
    }//GEN-LAST:event_jButtonRecMailsActionPerformed

    private void jButtonSendMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendMailsActionPerformed
        showList("sent_mails");
    }//GEN-LAST:event_jButtonSendMailsActionPerformed

    private void jButtonTrashMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTrashMailsActionPerformed
        showList("trash");
    }//GEN-LAST:event_jButtonTrashMailsActionPerformed

    private void jMenuItemRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRecActionPerformed
        jButtonRecMailsActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemRecActionPerformed

    private void jMenuItemSentMailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSentMailsActionPerformed
        jButtonSendMailsActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSentMailsActionPerformed

    private void jMenuItemTrashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTrashActionPerformed
        jButtonTrashMailsActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemTrashActionPerformed

    
    /////////////////////////////////
    //// PODSTAWOWE FUNKCJE POŁĄCZEŃ
    /////////////////////////////////
    
    // utworzenie buforów wyjścia i wejścia dla zwykłego połączenia
    private void createBuffers()
    {
        try {   
            // utworzenie obiektu writera do wysyłania zapytań do serwera
            //writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writer = new BufferedOutputStream(clientSocket.getOutputStream());
            obWriter = new ObjectOutputStream(writer);
            obWriter.flush();
            
            // utworzenie obiektu readera do odczytu odpowiedzi z serwera
            //reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            reader = new BufferedInputStream(clientSocket.getInputStream());
            obReader = new ObjectInputStream(reader);
        } catch (IOException ex) {
            Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            System.out.println("Bufory poprawnie utworzone");
        }
    }
    
    /*
    // utworzenie buforów wyjścia i wejścia dla połączenia SSL
    private void createBuffersSSL() throws IOException
    {
        // utworzenie obiektu readera do odczytu odpowiedzi z serwera
        //reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        
        // utworzenie obiektu writera do wysyłania zapytań do serwera
        //writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        // writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
    }
    
    // nawiązanie połączenia z serwerem przez SSL
    public void connectSSL() throws UnknownHostException, IOException{
        System.out.println("Próba połączenia z serwerem e-mail " + hostname + ":" + port);
        
        try
        {
            sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(hostname, port);
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            System.out.println("Połączenie z serwerem e-mail nawiązane!");
        }
        
        // utworzenie buforów wejścia i wyjścia
        createBuffers();
    }
    */
    
    
    // nawiązanie połączenia z serwerem
    public int connect(int port, String ip) throws UnknownHostException, IOException{
        System.out.println("Próba połączenia z serwerem e-mail " + ip + ":" + port);
        
        try
        {
            clientSocket = new Socket(ip, port);
            
            // clientSocket.setSoTimeout(50000);
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
            
            return -1;
        } finally {
            
            
            // System.out.println("Połączenie z serwerem e-mail nawiązane!");
        }
        
        // utworzenie buforów wejścia i wyjścia
        createBuffers();
        
        return 0;
    }
    
    public void disconnect()
    {
        try {
            if (clientSocket != null)
            {
                if (clientSocket.isConnected())
                {
                    clientSocket.close();
                    System.out.println("Rozłączono z serwerem");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////////
    //// WYMIANA POLECEŃ I DANYCH
    /////////////////////////////////
    
    // wysłanie komunikatu do serwera
    public void sendCommand() throws IOException
    {
       obWriter.reset();
        
       System.out.println("Klient: " + list.get(0));
       
       obWriter.writeObject(list);
       obWriter.flush();
    }
    
    /////////////////////////////
    //// FUNKCJONALNOŚCI MAILOWE
    /////////////////////////////
    
    // obsługa komunikatu
    public void handleCommand(MailClientApp client, String command, List<Object> frame)
    {
        String response = mailProtocol.handleServerCommand(command);
        
        System.out.println("Response from handleServerCommand(): " + response);
        
        //// wysyłanie rozkazów do serwera ////
        
        if (response.equals("MAIL"))
        {
            // wysłanie ramki z zawartością maila (wiadomości tekstowej)
            client.setMailFrame(client);
        }
        else if (response.equals("MAIL SUCCESS"))
        {  
            Map<String, Object> map = (Map<String, Object>) frame.get(1);
            
            System.out.println(map.get("sender"));
            
            String tmp_table[] = new String[4];
            
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
            map.put("received_date", ft.format(dNow));
            
            String title = map.get("title").toString();
            final int LENGHT = 37 /*43*/;
            if (title.length() > LENGHT)
            {
                title = title.substring(0, LENGHT) + "...";
            }
            
            tmp_table[0] = title;
            tmp_table[1] = ft.format(dNow);
            tmp_table[2] = map.get("title").toString();
            tmp_table[3] = map.get("sender").toString();
            // tmp_table[4] = map.get("received_date").toString();
            
            saveMailXML("rec", map);
            
            recModel.add(0, tmp_table);
            
            try {
                list.set(0, response);
                list.set(1, null);
                
                sendCommand();
            } catch (IOException ex) {
                Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        else if (response.equals("LOGOUT"))
        { 
            System.out.println("Wylogowywanie...");
            
            client.setVisible(false);
            
            logoutUser();
        }
        else if (!response.equals("done"))
        {
            try{
                list.set(0, response);
                list.set(1, null);
                
                // sendCommand(response);
                sendCommand();
            } catch(IOException e){
                System.out.println("Problem z wysłaniem rozkazu " + response + "!");
            }
        }
    }
    
    public static void saveMailXML(String mailType, Map<String, Object> m)
    {
         try {  
            // get some values from maildata
            String sender = m.get("sender").toString();
            String receiver = m.get("receiver").toString();
            
            String title = m.get("title").toString();
            String datetime = m.get("datetime").toString();
            // String txt_length = m.get("txt_length");
            String mail_txt = m.get("mail_txt").toString();
            
            String received_date = "";
            if (mailType.equals("rec"))
                received_date = m.get("received_date").toString();

            // returns an xml element object  
            // school is passed to make it root element in document  
            Element mail = new Element("mail");

            // created an document object, all elements will be added to it  
            // passes school as parameter to make it root element of document  
            Document document = new Document(mail);  

            // adding child attribute to student element
            mail.addContent(new Element("sender").setText(sender));
            mail.addContent(new Element("receiver").setText(receiver));
            mail.addContent(new Element("title").setText(title));
            mail.addContent(new Element("datetime").setText(datetime));
            if (mailType.equals("rec"))
                mail.addContent(new Element("received_date").setText(received_date));
            mail.addContent(new Element("mail_txt").setText(mail_txt));
            
            Map<Integer, Map> costam = (Map<Integer, Map>)m.get("attachments");
            int nr = costam.size();
            
            Map<Integer, Map> atts = (Map<Integer, Map>)m.get("attachments");
            Element atts_el[] = new Element[nr];
            
            // created other element to add to document  
            for (int i = 0; i < nr; i++)
            {
                Map<String, Object> tmp_map = atts.get(i);
                
                String att_title = tmp_map.get("att_title").toString();
                
                atts_el[i] = new Element("attachment");

                atts_el[i].addContent(new Element("att_title").setText(att_title));
                atts_el[i].addContent(new Element("att_length").setText(tmp_map.get("att_length").toString()));
       
                //// Zapisanie załącznika na dysk
                
                String path = "database/rec_mails/" + user + "/" + title;
                File file = (new File(path));
                file.mkdirs();

                try (FileOutputStream fis = new FileOutputStream("database/rec_mails/" + user + "/" + title + "/" + att_title)) 
                {
                    byte[] bytes = (byte[]) tmp_map.get("att_data");
                    
                    fis.write(bytes, 0, bytes.length);
                    fis.flush();
                    fis.close();
                }
                
                // get root element and added student element as a child of it  
                document.getRootElement().addContent(atts_el[i]);
            }

            // get object to see output of prepared document  
            XMLOutputter xmlOutput = new XMLOutputter();  

            // passsed System.out to see document content on console  
            xmlOutput.output(document, System.out);

            // passed fileWriter to write content in specified file
            String dirname = "";
            switch (mailType) {
                case "rec":
                    dirname = "database/rec_mails/";
                    break;
                case "sent":
                    dirname = "database/sent_mails/";
                    break;
                case "database/trash_mails/":
                    dirname = "database/trash_mails/";
                    break;
            }   
            
            new File(/*"database/rec_mails/"*/ dirname + user + "/" + title).mkdir();
            
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, new FileOutputStream (  
              /*"database/rec_mails/"*/ dirname + user + "/" + title + "/" + title + ".xml"));
           } catch (IOException io) {  
            System.out.println(io.getMessage());
           }  
    }
    
    //// POPULOWANIE LISTY MAILI ////
    
    private void populateRecMailsList() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //.SSSSSS'Z'
        SimpleDateFormat print = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        //print.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        print.setLenient(false);
        
        File dir = new File("database/rec_mails/" + user);          // przeglądanie katalogu z odebranymi mailami
        dir.mkdirs();                                               // utwórz katalog usera jeśli wcześniej nie utworzony
        
        String lista[];
        lista = dir.list();                                         // listowanie zawartości folderu z mailem (treść maila i załączniki)
        
        List<String[]> Mailsqueue = new LinkedList<>();             // kolejka maili do wyświetlenia na liście
        
        // PROCES populowania listy maili w interfejsie graficznym:
        for (String tabla1 : lista) {
            Path path = Paths.get("database/rec_mails/" + user + "/" + tabla1);
            
            File file = new File(path.toString());
            String pliki[] = file.list();
            
            String created = "";
            try {
                created = Files.getAttribute(path, "basic:creationTime").toString();
                
                System.out.println("File " + file.getName() + " created in " + created);
            } catch (IOException ex) {
                Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
            for (String name : pliki) {
                System.out.println("tabla1: " + tabla1);
                System.out.println("name: " + name);

                if (name.contains(".xml"))
                {
                    int x = name.lastIndexOf(".xml");
                    String s = name.substring(0, x);
                    System.out.println(s);

                    // Odczytanie daty odebrania wiadomości z pliku maila w katalogu roboczym
                    
                    final int LENGHT = 37 /*43*/;
                    
                    String temp_t = s;
                    if (temp_t.length() > LENGHT)
                    {
                        temp_t = temp_t.substring(0, LENGHT) + "...";
                    }

                    Date date = sdf.parse(created.substring(0, 19));
                    System.out.println("After parsing and to date: " + date.toString());
                    String tmp_table[] = new String[4];
                    tmp_table[0] = temp_t;
                    tmp_table[1] = print.format(sdf.parse(created));
                    tmp_table[2] = s;
                    
                    SAXBuilder saxBuilder = new SAXBuilder();
                    File xml = new File(file + "/" + name);

                    String sender = "";
                    
                    try {
                        Document document = saxBuilder.build(xml);
                        Element rootNode = document.getRootElement();

                        sender = rootNode.getChild("sender").getValue();
                    } catch (JDOMException | IOException e) {
                    }
                    
                    tmp_table[3] = sender;
                    
                    Mailsqueue.add(tmp_table);
                }
            }
        }
        
        // SORTOWANIE MAILI WEDŁUG DATY ODBIORU
        //for (String i[] : Mailsqueue)
        //{           
            String pom[];
 
            for (int i = 0; i < Mailsqueue.size(); i++)
            {
                for (int j = 0; j < Mailsqueue.size() - i - 1; j++)           //pętla wewnętrzna
                {
                    Date d1 = null;
                    Date d2 = null;
                    
                    String now = Mailsqueue.get(j)[1];
                    String next = Mailsqueue.get(j+1)[1];
                    
                    System.out.println("now: " + now);
                    System.out.println("next: " + next);
                    
                    try {
                        d1 = print.parse(now);
                        d2 = print.parse(next);
                    } catch (ParseException ex) {
                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (d1.getTime() < d2.getTime())
                    {
                      //zamiana miejscami
                      //pom = now;
                      //Mailsqueue.get(j)[1] = next;
                      //Mailsqueue.get(j+1)[1] = pom;
                        
                      pom = Mailsqueue.get(j);
                      Mailsqueue.set(j, Mailsqueue.get(j+1));
                      Mailsqueue.set(j+1, pom);
                    }
                }
            }
        //}
        
        // DODANIE DO WYŚWIETLENIA         
        for (String i[] : Mailsqueue)
        {
            recModel.addElement(i);
        }
    }
    
    
    private void populateSentMailsList() throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //.SSSSSS'Z'
        SimpleDateFormat print = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        print.setLenient(false);
        
        File dir = new File("database/sent_mails/" + user);          // przeglądanie katalogu z wysłanymi mailami
        dir.mkdirs();                                                // utwórz katalog usera jeśli wcześniej nie utworzony
        
        String lista[];
        lista = dir.list();                                         // listowanie zawartości folderu z mailem (treść maila i załączniki)
        
        List<String[]> Mailsqueue = new LinkedList<>();             // kolejka maili do wyświetlenia na liście
        
        // PROCES populowania listy wysłanych maili w interfejsie graficznym:
        for (String tabla1 : lista) {
            Path path = Paths.get("database/sent_mails/" + user + "/" + tabla1);
            
            File file = new File(path.toString());
            String pliki[] = file.list();
            
            String created = "";
            try {
                created = Files.getAttribute(path, "basic:creationTime").toString();
                
                System.out.println("File " + file.getName() + " created in " + created);
            } catch (IOException ex) {
                Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
            for (String name : pliki) {
                System.out.println("tabla1: " + tabla1);
                System.out.println("name: " + name);

                if (name.contains(".xml"))
                {
                    int x = name.lastIndexOf(".xml");
                    String s = name.substring(0, x);
                    System.out.println(s);

                    // Odczytanie daty odebrania wiadomości z pliku maila w katalogu roboczym
                    
                    final int LENGHT = 37 /*43*/;
                    
                    String temp_t = s;
                    if (temp_t.length() > LENGHT)
                    {
                        temp_t = temp_t.substring(0, LENGHT) + "...";
                    }

                    Date date = sdf.parse(created.substring(0, 19));
                    System.out.println("After parsing and to date: " + date.toString());
                    String tmp_table[] = new String[4];
                    tmp_table[0] = temp_t;
                    tmp_table[1] = print.format(sdf.parse(created));
                    tmp_table[2] = s;
                    
                    SAXBuilder saxBuilder = new SAXBuilder();
                    File xml = new File(file + "/" + name);

                    String receiver = "";
                    
                    try {
                        Document document = saxBuilder.build(xml);
                        Element rootNode = document.getRootElement();

                        receiver = rootNode.getChild("receiver").getValue();
                    } catch (JDOMException | IOException e) {
                    }
                    
                    tmp_table[3] = receiver;
                    
                    Mailsqueue.add(tmp_table);
                }
            }
        }
        
        // SORTOWANIE MAILI WEDŁUG DATY ODBIORU       
            String pom[];
 
            for (int i = 0; i < Mailsqueue.size(); i++)
            {
                for (int j = 0; j < Mailsqueue.size() - i - 1; j++)           //pętla wewnętrzna
                {
                    Date d1 = null;
                    Date d2 = null;
                    
                    String now = Mailsqueue.get(j)[1];
                    String next = Mailsqueue.get(j+1)[1];
                    
                    System.out.println("now: " + now);
                    System.out.println("next: " + next);
                    
                    try {
                        d1 = print.parse(now);
                        d2 = print.parse(next);
                    } catch (ParseException ex) {
                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (d1.getTime() < d2.getTime())
                    {
                      //zamiana miejscami
                      //pom = now;
                      //Mailsqueue.get(j)[1] = next;
                      //Mailsqueue.get(j+1)[1] = pom;
                        
                      pom = Mailsqueue.get(j);
                      Mailsqueue.set(j, Mailsqueue.get(j+1));
                      Mailsqueue.set(j+1, pom);
                    }
                }
            }
        
        // DODANIE DO WYŚWIETLENIA         
        for (String i[] : Mailsqueue)
        {
            sentModel.addElement(i);
        }
    }
    
    
    public void talking(MailClientApp t_client) 
    {
        final MailClientApp client = t_client;                    // Utworzenie obiektu klienta
                
        // dialog logowania
        loginDialog = new LoginDialog(new JFrame(), true);
        loginDialog.setVisible(true);                             // ustawienie, że okno logowania jest widoczne
        final int port = loginDialog.getServerPort();             // pobranie portu serwera z pola tekstowego
        final String servIP = loginDialog.getServerIP();          // pobranie ip serwera z pola tekstowego
                
        client.notConnected = true;                               // flaga początkowa - klient nie jest połączony z serwerem
        
        String logging;
        while(true)
        {
            // JEŚLI EKRAN LOGOWANIA NIE JEST WIDOCZNY
            logging = loginDialog.getReturnStatus();
            if (!loginDialog.isVisible())
            {
                if (logging.equals("RET_CANCEL") /*|| logging.equals("EXIT")*/ || logging.equals(""))
                {
                    client.dispose();
                    System.exit(0);
                }
            }

            System.out.println("Logging: " + logging);

            // WYSYŁANIE KOMUNIKATU Z LOGINEM I HASŁEM
            if (!logging.equals("") && !logging.equals("RET_CANCEL"))
            {
                // String ret = loginDialog.getReturnStatus();
                int deli = logging.indexOf("|");
                String login = logging.substring(0, deli);
                String passwd = logging.substring(deli+1);

                String req = mailProtocol.getCommand("request", "init");
                req += " LOGIN " + login + ", ";
                req += "PASSWD " + passwd;

                client.list.set(0, req);
                client.list.set(1, null);

                // Próba połączenia z serwerem
                if (client.notConnected)
                {
                    int status = -1;
                    try {
                        status = client.connect(port, servIP);
                    } catch (IOException ex) {
                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (status == 0)
                    {
                        client.notConnected = false;

                        Object frame;                   // ramka od serwera jako obiekt
                        List<Object> l;                 // ramka od serwera jako lista
                        String response;                // tekst odpowiedzi z serwera

                        try {
                            client.sendCommand();

                            frame = obReader.readObject();

                            l = (List<Object>)frame;
                            response = (String) l.get(0);


                            if (response.contains("HELLO OK"))
                            {
                                // zamknij okno logowania
                                loginDialog.doClose("OK");

                                user = login;

                                jLabelWelcome.setText("Zalogowano jako: " + login);

                                try {
                                    // wypisanie listy plików w konsoli
                                    client.populateRecMailsList();
                                    client.populateSentMailsList();
                                } catch (ParseException ex) {
                                    Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                client.setVisible(true);

                                client.handleCommand(client, response, l);      // obsłuż komendę "HELLO OK" lub "HELLO OK, NEW_MAILS"

                                
                                // obsługa odbierania odpowiedzi od serwera
                                while(true)
                                {
                                    try {
                                        // odczyt odpowiedzi
                                        frame = obReader.readObject();
                                        l = (List<Object>)frame;
                                        response = (String) l.get(0);

                                        System.out.println("Odpowiedź serwera: " + response);

                                        if (response != null && !response.equals("MAIL SUCCESS OK"))
                                        {
                                            System.out.println("Obsługiwane polecenie: " + response);

                                            client.handleCommand(client, response, l);
                                        }

                                        if (user.equals(""))
                                        {
                                            client.notConnected = true;
                                            break;
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            else if (response.equals("HELLO FAIL"))
                            {
                                client.disconnect();
                                client.notConnected = true;

                                loginDialog.setWarning("Zły login lub hasło");
                                loginDialog.setVisible(true);
                                loginDialog.reset();
                            }

                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else if (status == -1)
                    {
                        client.disconnect();
                        client.notConnected = true;

                        loginDialog.setWarning("Serwer jest niedostępny!");
                        loginDialog.setVisible(true);
                    }
                }


            }
        }
    }
    
    
    // GŁÓWNY WĄTEK KLIENTA
    public static void main(String arg[]) throws ClassNotFoundException, ParseException {
        /* Create and display the form */
        /*java.awt.EventQueue*/ SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        
                final MailClientApp client = new MailClientApp();       // instancja tej klasy
                
                //// NOWY WĄTEK DLA WYMIANY KOMUNIKATÓW Z SERWEREM
                Thread thread = new Thread() {
                
                    @Override
                    public void run() {
                
                        // komunikacja klient -> serwer
                        client.talking(client);
                        
                    }   // KONIEC DEFINICJI FUNKCJI run()
                };      // KONIEC KODU NOWEGO WĄTKU
                thread.start();
            }
        });
    }
    
    public void setMailFrame(MailClientApp client)
    {
        String comm = mailProtocol.getCommand("request", "mail");
        client.list.set(0, comm);
        client.list.set(1, mail_frame);
        
        try {
                client.sendCommand();
        } catch (IOException ex) {
                    Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void showList(String type)
    {
        switch (type) {
            case "received_mails":
                jLabelMsgDate.setText("Data odebrania");
                jLabelMsgSender.setText("Nadawca");
                
                m_listType = "rec";
                jListMails.setModel(recModel);
                break;
                
            case "sent_mails":
                jLabelMsgDate.setText("Data wysłania");
                jLabelMsgSender.setText("Odbiorca");
                
                m_listType = "sent";
                jListMails.setModel(sentModel);
                break;
                
            case "trash":
                jLabelMsgDate.setText("Data odebrania");
                jLabelMsgSender.setText("Nadawca");
                
                m_listType = "trash";
                jListMails.setModel(trashModel);
                break;
        }
    }
    
    
    private void logoutUser()
    {  
        // skasowanie listy maili w gui
        if (!recModel.isEmpty())
            recModel.clear();
        if (!sentModel.isEmpty())
            sentModel.clear();
        if (!trashModel.isEmpty())
            trashModel.clear();
        
        user = "";                                  // skasowanie usera z aktualnej pamięci
        loginDialog.showLoginDialog();              // pokaż dialog logowania
    }    
            
    private void closeClientApp()
    {
        try {
            clientSocket.close();                   // zamknięcie socketa klienta
        } catch (IOException ex) {
            Logger.getLogger(MailClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    private void addMailToSentList(String title)
    {
        
        
        sentModel.add(title);
    }
    */
            
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonRecMails;
    private javax.swing.JButton jButtonSendMails;
    private javax.swing.JButton jButtonTrashMails;
    private javax.swing.JButton jButtonWriteMail;
    private javax.swing.JLabel jLabelMsgDate;
    private javax.swing.JLabel jLabelMsgSender;
    private javax.swing.JLabel jLabelMsgTitle;
    private static javax.swing.JLabel jLabelWelcome;
    private javax.swing.JList jListMails;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemInfo;
    private javax.swing.JMenuItem jMenuItemLogout;
    private javax.swing.JMenuItem jMenuItemRec;
    private javax.swing.JMenuItem jMenuItemSentMails;
    private javax.swing.JMenuItem jMenuItemSettings;
    private javax.swing.JMenuItem jMenuItemTrash;
    private javax.swing.JMenuItem jMenuItemWriteMail;
    private javax.swing.JMenu jMenuWriteMail;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables

}