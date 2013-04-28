
package cs235a5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/** @brief This class contains all the UI communicate to the server

    This Class Contains the methods to set up the UI for communicating to the 
    * server. the UI starts small just showing the Login Panel. if the login is
    * successful the UI expands to show the files available on the server and to
    * upload a new file
    @author Robert Fletcher 
    @file CloudDialog.java
    @date April 2013
    */
public class CloudDialog extends JFrame{
    
    /**
     * Class Constructor Sets up the UI and the DataSet for the Class
     * @param DataSet DataSet the dataSet for the class
     */
    public CloudDialog(DataSet[] db, VisualiserGUI vg, TabPanel tp){
         m_db = db;
         m_TP = tp;
         context = this;
         m_guiContext = vg;
         initMainFrame();
         intLoginPanel();
         m_DownloadPanel = new JPanel();
         initListPannel(new String[0]);
         this.getContentPane().add(m_DownloadPanel);
         intUloadPanel();
         this.setVisible(true);
    }
    /**
     * Sets up the the Main Frame with its size property, resize property
     */
    private void initMainFrame(){
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(MAINFRAMESIZE);
        BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        this.getContentPane().setLayout(bl);
        this.setPreferredSize(MAINFRAMESIZE);
        this.setResizable(false);
        this.pack();
    }
    /**
     * Converts the 2 Dimensional array from the CloudIO class to a One
     * Dimensional array for the JList. 
     * @param String[][] A 2 Dimensional from the CloudIO;
     * @return String[] String Array for a List
     */
    private String[] convertList(String[][] l){
        String[] list = new String[l.length];
        for(int i =0; i<list.length;i++){
            String input = l[i][1];
            System.out.println(input);
            list[i] = input;
        }
        
        return list;
    }
    /**
     * Sets up the Login Panel with the Label and the TextField and Button
     */
    private void intLoginPanel(){
        m_loginPanel = new JPanel();
        m_loginPanel.setLayout(new FlowLayout());
        m_userLabel = new JLabel("Username: ");
        m_userFeild = new JTextField(10);
        m_passLabel = new JLabel("Password: ");
        m_passFeild = new JPasswordField(10);
        m_login = new JButton("Login");
        
        m_login.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e){
                String username = m_userFeild.getText();
                String pass =  String.valueOf(m_passFeild.getPassword());
                System.out.println("Username: "+username+"\nPassword: "+pass);
                m_sid =CLOUD.login(pass, pass);
                if(m_sid!=null){
                    m_data = CLOUD.List(m_sid);
                    if(m_data!=null){
                        initListPannel(convertList(m_data));
                        m_passFeild.setEnabled(false);
                        m_userFeild.setEnabled(false);
                        m_login.setEnabled(false);
                        Thread t = new expand();
                        t.start();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,
                    "Wrong Username and or password",
                    "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                    context.dispose();
                }
               
            }
            
        });
        m_loginPanel.add(m_userLabel);
        m_loginPanel.add(m_userFeild);
        m_loginPanel.add(m_passLabel);
        m_loginPanel.add(m_passFeild);
        m_loginPanel.add(m_login);
        m_loginPanel.validate();
        m_loginPanel.setOpaque(true);
        this.getContentPane().add(m_loginPanel);
    }
    /**
     * Sets up the upload panel with the label and Button with Action Listener
     */
    private void intUloadPanel(){
       
        m_UploadPanel = new JPanel();
        m_UploadPanel.setLayout(new FlowLayout());
        m_uploadLabel = new JLabel("Upload File");
        m_upload = new JButton("Upload");
        m_upload.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.err.println(file.getAbsoluteFile());
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                
                        SaveDialog s = new SaveDialog(m_db,m_TP);
                        if(s.SaveFile(file)){

                            if(CLOUD.upload(m_sid, file)!=null){
                                JOptionPane.showMessageDialog(null,
                                "Uplaod Succsesful",
                                "Upload Status",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }else{
                                JOptionPane.showMessageDialog(null,
                                "Failed to upload the file",
                                "Uplpad Failed",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,
                                "error making file",
                                "Uplpad Failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                    Logger.getLogger(
                          SaveDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            }
        });
        m_UploadPanel.add(m_uploadLabel);

        m_UploadPanel.add(m_upload);
        
        m_UploadPanel.validate();
        m_UploadPanel.setOpaque(true);
        this.getContentPane().add(m_UploadPanel);
    }
    /**
     * Sets up the listpannel with a List and Scroll Pane and Button
     * @param String[] List a list for use for the JList, to Display the files
     * on the server
     */
    
    private void initListPannel(String[] list){
      
        m_DownloadPanel.removeAll();
        m_List = new JList(list);
        
        m_listPanel = new JPanel();
        m_listPanel.setPreferredSize(PANELSIZE);
        m_listPanel.setBounds(0, 0, 200, 200);
        
        m_buttonPannel = new JPanel();
        m_buttonPannel.setLayout(new BorderLayout());
        m_GetFile = new JButton("Use Selected File");
        m_GetFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String fileID = m_data[m_List.getSelectedIndex()][0];
                String path = CLOUD.getFilePath(m_sid, fileID);
                File f = CLOUD.DownloadFile(path);
                
                System.err.println("Path = "+f.getAbsolutePath());
                OpenDialog o = new OpenDialog(m_db,m_TP,m_guiContext);
                o.ReadFile(f);
                //
                
               
            }
        });
        m_buttonPannel.add( m_GetFile, BorderLayout.CENTER);
        
        m_listPanel.setBackground(Color.YELLOW);
       
        m_listPanel.setLayout(new BorderLayout());     
        m_listPanel.add(new JScrollPane(m_List), BorderLayout.CENTER);
        m_listPanel.add(m_buttonPannel, BorderLayout.SOUTH);  
        m_listPanel.repaint();
        m_listPanel.validate();
        m_DownloadPanel.add(m_listPanel);
        m_DownloadPanel.validate();   
    }

    
    private TabPanel m_TP;
    private VisualiserGUI m_guiContext;
    private DataSet[] m_db;
    private String[][] m_data;
    private String[] m_IOlist = new String[0];
    private String m_sid;
    private final DefaultListModel m_dlm = new DefaultListModel();
    private CloudDialog context; 
    private JLabel m_userLabel, m_passLabel, m_uploadLabel;
    private JList  m_List = new JList();
    private JTextField m_userFeild, m_FilePath;
    private JPasswordField m_passFeild;
    private JPanel m_loginPanel,m_UploadPanel,m_listPanel,m_buttonPannel, 
            m_DownloadPanel; 
    private JButton m_login,m_browse,m_upload,m_GetFile;
    private final Dimension MAINFRAMESIZE = new Dimension(550,60);
    private final Dimension PANELSIZE = new Dimension(400,200);
    private final int MAXSIZE = 350;
    private final int REFRESHRATE = 10;       
    private final CloudIO CLOUD = new CloudIO();
    private final String CLASS = "CloudDialog"; 
    private File file = new File(System.getProperty("user.dir")+"/"+getDate()+".xml");
    
    
    /**
     * Thread class for UI animation
     */
    private class expand extends Thread{
    
        @Override
        /**
         * Run class that overrides the Thread.run() method. Will upload.
         */
        public void run(){
            for(int i = MAINFRAMESIZE.height;i<MAXSIZE;i+=REFRESHRATE){
                context.setSize(new Dimension(MAINFRAMESIZE.width,i));
                try {
                    this.sleep(1);//refresh 
                } catch (InterruptedException ex) {
                   
                }
            }
        }
    }
    
     private String getDate(){
               DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
               //get current date time with Date()
               Date date = new Date();
               System.out.println(dateFormat.format(date));
         
               //get current date time with Calendar()
               Calendar cal = Calendar.getInstance();
               return(dateFormat.format(cal.getTime()));
               
           
           
     }    
    
    

    
    
}
