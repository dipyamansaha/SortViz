// SortViz - An Interactive Visualisation Tool for Sorting Algorithms

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

public class SortViz
{
    //FRAME
    private JFrame jf;
    //GENERAL VARIABLES
    int len = 50;
    private int off = 0;
    private int curAlg = 0;
    private int spd = 20;
    private long time = 0;
    private int compare = 0;
    private int acc = 0;
    //PLOT VARIABLES
    private final int SIZE = 600;
    private int current = -1;
    private int check = -1;
    private int width = SIZE / len;
    private int type = 0;
    //ARRAYS
    private int[] list;
    private String[] types = {"Bar Chart", "Dot Chart"};
    private String[] algorithms = {"Selection Sort",
            "Bubble Sort",
            "Insertion Sort",
            "Merge Sort",
            "Quick Sort",
            "Heap Sort"};
    private String[] algInfo = {"Time Complexity Analysis:\n\nBest Case: O(n^2)\nAverage case: O(n^2)\nWorst Case: O(n^2)",
            "Time Complexity Analysis:\n\nBest Case: O(n)\nAverage Case: O(n^2)\nWorst Case: O(n^2)",
            "Time Complexity Analysis:\n\nBest Case: O(n)\nAverage Case: O(n^2)\nWorst Case: O(n^2)",
            "Time Complexity Analysis:\n\nBest Case: O(nlogn)\nAverage Case: O(nlogn)\nWorst Case: O(nlogn)",
            "Time Complexity Analysis:\n\nBest Case: O(nlogn)\nAverage Case: O(nlogn)\nWorst Case: O(n^2)",
            "Time Complexity Analysis:\n\nBest Case: O(nlogn)\nAverage Case: O(nlogn)\nWorst Case: O(nlogn)"};
    //BOOLEANS
    boolean sorting = false;
    private boolean shuffled = true;
    //UTIL OBJECTS
    SortingAlgorithms algorithm = new SortingAlgorithms();
    Random r = new Random();
    //PANELS
    JPanel tools = new JPanel();
    PlotCanvas canvas;
    //LABELS
    JLabel delayL = new JLabel("Delay:");
    JLabel dmsL = new JLabel(spd + " ms");
    JLabel sizeL = new JLabel("Size:");
    JLabel lenL = new JLabel(len + "");
    JLabel compareL = new JLabel("Comparisons made: " + compare);
    JLabel accessL = new JLabel("Array accesses: " + acc);
    JLabel timeL = new JLabel("Elapsed Time: " + time + " ms");
    JLabel algorithmL = new JLabel("Sorting Algorithm:");
    JLabel typeL = new JLabel("Plot Type:");
    //DROP DOWN BOX
    JComboBox alg = new JComboBox(algorithms);
    JComboBox plot = new JComboBox(types);
    JTextArea information = new JTextArea(algInfo[curAlg]);
    //BUTTONS
    JButton sort = new JButton("SORT");
    JButton shuffle = new JButton("SHUFFLE");
    JButton about = new JButton("About");
    //SLIDERS
    JSlider size = new JSlider(JSlider.HORIZONTAL, 1, 6, 1);
    JSlider speed = new JSlider(JSlider.HORIZONTAL, 0, 100, spd);
    //BORDER STYLE
    Border lineBorder = BorderFactory.createLineBorder(Color.black);

    //CONSTRUCTOR
    public SortViz()
    {
        shuffleList();    //CREATE THE LIST
        initialise();    //INITIALISE THE GUI
    }

    public void createList()
    {
        list = new int[len];    //CREATES A LIST EQUAL TO THE LENGTH
        for(int i = 0; i < len; i++)
        {    //FILLS THE LIST FROM 1-LEN
            list[i] = i + 1;
        }
    }

    public void shuffleList()
    {
        createList();
        for(int a = 0; a < 500; a++)
        {    //SHUFFLE RUNS 500 TIMES
            for(int i = 0; i < len; i++)
            {    //ACCESS EACH ELEMENT OF THE LIST
                int rand = r.nextInt(len);    //PICK A RANDOM NUM FROM 0-LEN
                int temp = list[i];            //SETS TEMP INT TO CURRENT ELEMENT
                list[i] = list[rand];        //SWAPS THE CURRENT INDEX WITH RANDOM INDEX
                list[rand] = temp;            //SETS THE RANDOM INDEX TO THE TEMP
            }
        }
        sorting = false;
        shuffled = true;
    }

    public void initialise()
    {
        //SET UP FRAME
        jf = new JFrame();
        jf.setSize(800, 635);
        jf.setTitle("SortViz");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        jf.getContentPane().setLayout(null);

        //SET UP TOOLBAR
        tools.setLayout(null);
        tools.setBounds(5, 5, 180, 590);
        Border outer = BorderFactory.createEmptyBorder(4, 4, 4, 4);
        Border inner = BorderFactory.createTitledBorder(lineBorder, "CONTROLLER");
        tools.setBorder(BorderFactory.createCompoundBorder(outer, inner));
        tools.setBackground(new Color(244, 185, 113));

        //SET UP ALGORITHM LABEL
        algorithmL.setHorizontalAlignment(JLabel.CENTER);
        algorithmL.setBounds(30, 20, 120, 25);
        tools.add(algorithmL);

        //SET UP DROP DOWN
        alg.setBounds(30, 45, 120, 25);
        alg.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(alg);

        //SET UP PLOT TYPE LABEL
        typeL.setHorizontalAlignment(JLabel.CENTER);
        typeL.setBounds(40, 80, 100, 25);
        tools.add(typeL);

        //SET UP PLOT TYPE DROP DOWN
        plot.setBounds(30, 105, 120, 25);
        plot.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(plot);

        //SET UP SORT BUTTON
        sort.setBounds(40, 150, 100, 25);
        sort.setFont(new Font(Font.DIALOG, Font.BOLD | Font.ITALIC,  12));
        sort.setBackground(new Color(128, 13, 0));
        sort.setForeground(Color.white);
        tools.add(sort);

        //SET UP SHUFFLE BUTTON
        shuffle.setBounds(40, 190, 100, 25);
        shuffle.setFont(new Font(Font.DIALOG, Font.BOLD,  12));
        shuffle.setBackground(Color.black);
        shuffle.setForeground(Color.white);
        tools.add(shuffle);

        //SET UP DELAY LABEL
        delayL.setHorizontalAlignment(JLabel.LEFT);
        delayL.setBounds(10, 230, 50, 25);
        delayL.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(delayL);

        //SET UP MS LABEL
        dmsL.setHorizontalAlignment(JLabel.LEFT);
        dmsL.setBounds(135, 230, 50, 25);
        dmsL.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(dmsL);

        //SET UP SPEED SLIDER
        speed.setMajorTickSpacing(5);
        speed.setBounds(55, 230, 75, 25);
        speed.setPaintTicks(false);
        tools.add(speed);

        //SET UP SIZE LABEL
        sizeL.setHorizontalAlignment(JLabel.LEFT);
        sizeL.setBounds(10, 275, 50, 25);
        sizeL.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(sizeL);

        //SET UP LEN LABEL
        lenL.setHorizontalAlignment(JLabel.LEFT);
        lenL.setBounds(140, 275, 50, 25);
        lenL.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC,  12));
        tools.add(lenL);

        //SET UP SIZE SLIDER
        size.setMajorTickSpacing(50);
        size.setBounds(55, 275, 75, 25);
        size.setPaintTicks(false);
        tools.add(size);

        //SET UP COMPARISONS LABEL
        compareL.setHorizontalAlignment(JLabel.LEFT);
        compareL.setBounds(10, 315, 200, 25);
        tools.add(compareL);

        //SET UP ARRAY ACCESS LABEL
        accessL.setHorizontalAlignment(JLabel.LEFT);
        accessL.setBounds(10, 340, 200, 25);
        tools.add(accessL);

        //SET UP TIME LABEL
        timeL.setHorizontalAlignment(JLabel.LEFT);
        timeL.setBounds(10, 370, 200, 25);
        tools.add(timeL);

        //SET UP INFO AREA
        information.setBounds(10, 410, 160, 125);
        information.setEditable(false);
        information.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        tools.add(information);

        //SET UP ABOUT BUTTON
        about.setBounds(40, 550, 100, 25);
        tools.add(about);

        //SET UP CANVAS FOR PLOT
        canvas = new PlotCanvas();
        canvas.setBounds(190, 0, SIZE, SIZE);
        canvas.setBorder(BorderFactory.createLineBorder(Color.black));
        jf.getContentPane().add(tools);
        jf.getContentPane().add(canvas);

        //ADD ACTION LISTENERS
        alg.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                curAlg = alg.getSelectedIndex();
                information.setText(algInfo[curAlg]);
            }

        });
        plot.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                type = plot.getSelectedIndex();
                shuffleList();
                reset();
                Update();
            }
        });
        sort.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(shuffled)
                {
                    sorting = true;
                    compare = 0;
                    acc = 0;
                    time = 0;
                }

            }
        });
        shuffle.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                shuffleList();
                reset();
            }
        });
        speed.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent arg0)
            {
                spd = (int) speed.getValue();
                dmsL.setText(spd + " ms");
            }
        });
        size.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int val = size.getValue();
                if(size.getValue() == 5)
                    val = 4;
                len = val * 50;
                lenL.setText(len + "");
                if(list.length != len)
                    shuffleList();
                reset();
            }

        });
        about.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(jf, "	                          SortViz\n"
                        + "- An Interactive Sorting Visualisation Tool\n"
                        + "                 - Dipyaman Saha\n", "About", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(""));
            }
        });

        sorting();
    }

    //SORTING STATE
    public void sorting()
    {
        if(sorting)
        {
            try
            {
                switch(curAlg)
                {    //CURRENT ALGORITHM IS EXECUTED
                    case 0:
                        algorithm.selectionSort();
                        break;
                    case 1:
                        algorithm.bubbleSort();
                        break;
                    case 2:
                        algorithm.insertionSort(0, len - 1);
                        break;
                    case 3:
                        algorithm.mergeSort(0, len - 1);
                        break;
                    case 4:
                        algorithm.quickSort(0, len - 1);
                        break;
                    case 5:
                        algorithm.heapSort();
                        break;
                    default:
                        algorithm.quickSort(0, len - 1);
                        break;
                }
            }
            catch(IndexOutOfBoundsException e)
            {
            }    //EXCEPTION HANDLER IN CASE LIST ACCESS IS OUT OF BOUNDS
        }
        reset();    //RESET
        pause();    //GO INTO PAUSE STATE
    }

    //PAUSE STATE
    public void pause()
    {
        int i = 0;
        while(!sorting)
        {    //LOOP RUNS UNTIL SORTING STARTS
            i++;
            if(i > 100)
                i = 0;
            try
            {
                Thread.sleep(1);
            }
            catch(Exception e)
            {
            }
        }
        sorting();    //EXIT THE LOOP AND START SORTING
    }

    //RESET SOME VARIABLES
    public void reset()
    {
        sorting = false;
        current = -1;
        check = -1;
        off = 0;
        Update();
    }

    //DELAY METHOD
    public void delay()
    {
        try
        {
            Thread.sleep(spd);
        }
        catch(Exception e)
        {
        }
    }

    //UPDATES THE PLOT AND LABELS
    public void Update()
    {
        width = SIZE / len;
        canvas.repaint();
        compareL.setText("Comparisons made: " + compare);
        accessL.setText("Array accesses: " + acc);
        timeL.setText("Elapsed time: " + time + " ms");
    }

    //MAIN METHOD
    public static void main(String[] args)
    {
        new SortViz();
    }

    //SUB CLASS TO CONTROL THE PLOT
    class PlotCanvas extends JPanel
    {

        public PlotCanvas()
        {
            setBackground(new Color(182, 250, 179));
            /*setBackground(Color.yellow);*/
        }

        //PAINTS THE PLOT
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for(int i = 0; i < len; i++)
            {    //RUNS TROUGH EACH ELEMENT OF THE LIST
                int HEIGHT = list[i] * width;    //SETS THE HEIGHT OF THE ELEMENT ON THE PLOT
                if(type == 0)
                {        //BAR CHART TYPE
                    g.setColor(new Color(15, 124, 9));    //DEFAULT COLOR
                    if(current > -1 && i == current)
                    {
                        g.setColor(Color.yellow);    //COLOR OF CURRENT
                    }
                    if(check > -1 && i == check)
                    {
                        g.setColor(Color.red);    //COLOR OF CHECKING
                    }
                    //DRAWS THE BAR AND THE BLACK OUTLINE
                    g.fillRect(i * width, SIZE - HEIGHT, width, HEIGHT);
                    g.setColor(Color.black);
                    g.drawRect(i * width, SIZE - HEIGHT, width, HEIGHT);
                } else if(type == 1)
                {    //DOT CHART TYPE
                    g.setColor(new Color(15, 124, 9));    //DEFAULT COLOR
                    if(current > -1 && i == current)
                    {
                        g.setColor(Color.yellow);    //COLOR OF CURRENT
                    }
                    if(check > -1 && i == check)
                    {
                        g.setColor(Color.red);    //COLOR OF CHECKING
                    }
                    //DRAWS THE DOT
                    g.fillOval(i * width, SIZE - HEIGHT, width, width);
                }
            }
        }
    }

    //SUB CLASS FOR ALGORITHMS
    class SortingAlgorithms
    {
        public void selectionSort()
        {
            long startTime = System.currentTimeMillis();

            int c = 0;
            while(c < len && sorting)
            {
                time = (System.currentTimeMillis() - startTime);

                int sm = c;
                current = c;
                for(int i = c + 1; i < len; i++)
                {
                    if(!sorting)
                        break;
                    if(list[i] < list[sm])
                    {
                        sm = i;
                    }
                    check = i;
                    compare++;
                    acc += 2;
                    Update();
                    delay();
                }
                if(c != sm)
                    swap(c, sm);
                c++;
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        public void insertionSort(int start, int end)
        {
            long startTime = System.currentTimeMillis();

            for(int i = start + 1; i <= end; i++)
            {
                time = (System.currentTimeMillis() - startTime);

                current = i;
                int j = i;
                while(list[j] < list[j - 1] && sorting)
                {
                    swap(j, j - 1);
                    check = j;
                    compare++;
                    acc += 2;
                    Update();
                    delay();
                    if(j > start + 1)
                        j--;
                }
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        public void bubbleSort()
        {
            long startTime = System.currentTimeMillis();

            int c = 1;
            boolean noswaps = false;
            while(!noswaps && sorting)
            {
                time = (System.currentTimeMillis() - startTime);

                current = len - c;
                noswaps = true;
                for(int i = 0; i < len - c; i++)
                {
                    if(!sorting)
                        break;
                    if(list[i + 1] < list[i])
                    {
                        noswaps = false;
                        swap(i, i + 1);
                    }
                    check = i + 1;
                    compare++;
                    acc += 2;
                    Update();
                    delay();
                }
                c++;
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        void merge(int l, int m, int r)
        {
            int n1 = m - l + 1;
            int n2 = r - m;

            int L[] = new int[n1];
            int R[] = new int[n2];

            for(int i = 0; i < n1; i++)
            {
                L[i] = list[l + i];
                acc++;
            }
            for(int j = 0; j < n2; j++)
            {
                R[j] = list[m + 1 + j];
                acc++;
            }
            int i = 0, j = 0;

            int k = l;
            while(i < n1 && j < n2 && sorting)
            {
                check = k;
                if(L[i] <= R[j])
                {
                    list[k] = L[i];
                    acc++;
                    i++;
                } else
                {
                    list[k] = R[j];
                    acc++;
                    j++;
                }
                compare++;
                Update();
                delay();
                k++;
            }

            while(i < n1 && sorting)
            {
                list[k] = L[i];
                acc++;
                i++;
                k++;
                Update();
                delay();
            }

            while(j < n2 && sorting)
            {
                list[k] = R[j];
                acc++;
                j++;
                k++;
                Update();
                delay();
            }
        }

        public void mergeSort(int l, int r)
        {
            long startTime = System.currentTimeMillis();

            if(l < r)
            {
                time = (System.currentTimeMillis() - startTime);

                int m = (l + r) / 2;
                current = r;
                mergeSort(l, m);
                mergeSort(m + 1, r);

                merge(l, m, r);
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        public void quickSort(int lo, int hi)
        {
            long startTime = System.currentTimeMillis();

            if(!sorting)
                return;
            current = hi;
            if(lo < hi)
            {
                time = (System.currentTimeMillis() - startTime);

                int p = partition(lo, hi);
                quickSort(lo, p - 1);
                quickSort(p + 1, hi);
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        public int partition(int lo, int hi)
        {
            int pivot = list[hi];
            acc++;
            int i = lo - 1;
            for(int j = lo; j < hi; j++)
            {
                check = j;
                if(!sorting)
                    break;
                if(list[j] < pivot)
                {
                    i++;
                    swap(i, j);
                }
                compare++;
                acc++;
                Update();
                delay();
            }
            swap(i + 1, hi);
            Update();
            delay();
            return i + 1;
        }

        public void heapSort()
        {
            long startTime = System.currentTimeMillis();

            heapify(len);
            int end = len - 1;
            while(end > 0 && sorting)
            {
                time = (System.currentTimeMillis() - startTime);

                current = end;
                swap(end, 0);
                end--;
                siftDown(0, end);
                Update();
                delay();
            }

            long endTime = System.currentTimeMillis();

            time = (endTime - startTime);
        }

        public void heapify(int n)
        {
            int start = iParent(n - 1);
            while(start >= 0 && sorting)
            {
                siftDown(start, n - 1);
                start--;
                Update();
                delay();
            }
        }

        public void siftDown(int start, int end)
        {
            int root = start;
            while(iLeftChild(root) <= end && sorting)
            {
                int child = iLeftChild(root);
                int swap = root;
                check = root;
                if(list[swap] < list[child])
                {
                    swap = child;
                }
                if(child + 1 <= end && list[swap] < list[child + 1])
                {
                    swap = child + 1;
                }
                if(swap == root)
                {
                    return;
                } else
                {
                    swap(root, swap);
                    check = root;
                    root = swap;
                }
                compare += 3;
                acc += 4;
                Update();
                delay();
            }
        }

        public int iParent(int i)
        {
            return ((i - 1) / 2);
        }

        public int iLeftChild(int i)
        {
            return 2 * i + 1;
        }

        public void swap(int i1, int i2)
        {
            int temp = list[i1];
            acc++;
            list[i1] = list[i2];
            acc += 2;
            list[i2] = temp;
            acc++;
        }
    }
}
