import javax.swing.*;     // GUI components (JFrame, JButton, JLabel)
import java.awt.*;        // Color, Font
import java.awt.event.*;  // Event handling (button click, timer)

public class QuizApp {

    public static void main(String[] args) {
        new LoginPage();  // Program start → Login Page open
    }
}

// ================= LOGIN PAGE =================
class LoginPage extends JFrame implements ActionListener {

    JTextField nameField; // User name input

    LoginPage() {
        setBounds(200,100,800,500); // Window size + position
        setLayout(null);            // Manual layout
        getContentPane().setBackground(new Color(245,245,245)); // Background color

        JLabel title = new JLabel("MINI QUIZ"); // Title text
        title.setBounds(250,50,300,50);
        title.setFont(new Font("Arial",Font.BOLD,36)); // Big font
        title.setForeground(new Color(30,144,255));    // Blue color
        add(title);

        JLabel name = new JLabel("Enter Your Name:"); // Instruction
        name.setBounds(300,150,200,30);
        add(name);

        nameField = new JTextField(); // Input box
        nameField.setBounds(300,190,200,30);
        add(nameField);

        JButton next = new JButton("Start Quiz"); // Start button
        next.setBounds(330,250,130,35);
        next.addActionListener(this); // Click event connect
        add(next);

        setVisible(true); // Show window
    }

    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText(); // Get user input

        if(name.isEmpty()) { // Validation check
            JOptionPane.showMessageDialog(this, "Please enter your name!");
            return;
        }

        setVisible(false);        // Hide login page
        new QuizPage(name);       // Go to Quiz Page
    }
}

// ================= QUIZ PAGE =================
class QuizPage extends JFrame implements ActionListener {

    String name; // Store user name

    JLabel question, timerLabel; // Question + timer display
    JRadioButton op1, op2, op3, op4; // Options
    ButtonGroup bg; // Ensures only one option selected
    JButton next;

    int qIndex = 0, score = 0, time = 15; // Question index, score, timer

    String[] questions = {
        "What is Java?", // Question list
        "Which is not OOP concept?",
        "JVM stands for?",
        "Which keyword is used for inheritance?",
        "Which data type is used for decimals?",
        "Which method is entry point?",
        "Which is not Java feature?",
        "Which operator is used for comparison?",
        "Which collection allows duplicates?",
        "Which keyword is used to create object?"
    };

    String[][] options = {
        {"Language","Animal","Car","Food"}, // Options for Q1
        {"Inheritance","Encapsulation","Compilation","Polymorphism"},
        {"Java Virtual Machine","Java Very Much","Joint VM","None"},
        {"extends","implements","inherits","super"},
        {"int","float","char","boolean"},
        {"main()","start()","run()","init()"},
        {"Platform Independent","Secure","Pointer","Robust"},
        {"==","=","!=","<>"},
        {"Set","Map","List","None"},
        {"new","create","class","this"}
    };

    int[] answers = {0,2,0,0,1,0,2,0,2,0}; 
    // Correct answers (index based)

    QuizPage(String name) {
        this.name = name; // Save name

        setBounds(200,100,800,500);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        timerLabel = new JLabel("Time: 15"); // Timer display
        timerLabel.setBounds(650,20,100,30);
        add(timerLabel);

        question = new JLabel(); // Question text
        question.setBounds(50,100,600,30);
        add(question);

        // Options setup
        op1 = new JRadioButton();
        op2 = new JRadioButton();
        op3 = new JRadioButton();
        op4 = new JRadioButton();

        op1.setBounds(50,150,300,30);
        op2.setBounds(50,180,300,30);
        op3.setBounds(50,210,300,30);
        op4.setBounds(50,240,300,30);

        bg = new ButtonGroup(); // Only one option selectable
        bg.add(op1); bg.add(op2); bg.add(op3); bg.add(op4);

        add(op1); add(op2); add(op3); add(op4);

        next = new JButton("Next"); // Next button
        next.setBounds(350,320,100,35);
        next.addActionListener(this);
        add(next);

        loadQuestion(); // Load first question

        // Timer logic (runs every 1 second)
        Timer timer = new Timer(1000, e -> {
            time--; // Decrease time
            timerLabel.setText("Time: " + time); // Update UI

            if(time == 0) next.doClick(); // Auto next if time ends
        });
        timer.start();

        setVisible(true);
    }

    void loadQuestion() {
        // Set question text
        question.setText("Q" + (qIndex+1) + ": " + questions[qIndex]);

        // Set options
        op1.setText(options[qIndex][0]);
        op2.setText(options[qIndex][1]);
        op3.setText(options[qIndex][2]);
        op4.setText(options[qIndex][3]);

        bg.clearSelection(); // Clear previous answer
        time = 15;           // Reset timer
    }

    public void actionPerformed(ActionEvent e) {

        int selected = -1; // Store selected answer

        // Check which option selected
        if(op1.isSelected()) selected = 0;
        else if(op2.isSelected()) selected = 1;
        else if(op3.isSelected()) selected = 2;
        else if(op4.isSelected()) selected = 3;

        // Check correct answer
        if(selected == answers[qIndex]) score++;

        qIndex++; // Move to next question

        if(qIndex < questions.length) {
            loadQuestion(); // Load next
        } else {
            setVisible(false);
            new ResultPage(name, score); // Show result
        }
    }
}

// ================= RESULT PAGE =================
class ResultPage extends JFrame implements ActionListener {

    JButton playAgain, exit;

    ResultPage(String name, int score) {

        setBounds(200,100,800,500);
        setLayout(null);

        JLabel heading = new JLabel("🎉 Thank you, " + name + "!"); // Show name
        heading.setBounds(200,80,400,40);
        add(heading);

        JLabel result = new JLabel("Your Score: " + score + " / 10"); // Show score
        result.setBounds(280,150,300,40);
        add(result);

        JLabel grade = new JLabel();
        grade.setBounds(300,200,200,30);

        // Grade calculation
        if(score >= 8) grade.setText("Grade: A 🎯");
        else if(score >= 5) grade.setText("Grade: B 👍");
        else grade.setText("Grade: C 😅");

        add(grade);

        // Play again button
        playAgain = new JButton("Play Again");
        playAgain.setBounds(250,300,130,40);
        playAgain.addActionListener(this);
        add(playAgain);

        // Exit button
        exit = new JButton("Exit");
        exit.setBounds(420,300,130,40);
        exit.addActionListener(this);
        add(exit);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == playAgain) {
            setVisible(false);
            new LoginPage(); // Restart app
        } else {
            System.exit(0); // Close program
        }
    }
}