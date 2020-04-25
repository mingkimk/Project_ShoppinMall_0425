
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import DB.BasketlistDAO;
import DB.BasketlistDTO;

public class Basketlist extends JFrame {
	Scanner in = new Scanner(System.in);
	String header[] = { "��ǰ�ڵ�", "��ǰ�̸�", "����","�ܰ�","����", "üũ" };
	JTabbedPane tabpane = new JTabbedPane();
	DefaultTableModel tablemodel = new DefaultTableModel(header, 0);
	JTable table = new JTable(tablemodel);
	JScrollPane tableScroll = new JScrollPane(table);

	// center panel
	JPanel tab_center = new JPanel();
	JPanel tab_south = new JPanel();
	JPanel south_north = new JPanel();

	JTextField total = new JTextField(5);

	int modIntRow = -1;

	String fileName = "data.txt";

	BasketlistDAO dao = BasketlistDAO.getInstance();
	BasketlistDTO dto = null;
	ArrayList<String[]> initList = new ArrayList<>();
	ArrayList<String[]> goodsList = new ArrayList<>();
	ArrayList<BasketlistDTO> rlist = new ArrayList<>();
	String id = null;
	int chk = 1;

	public Basketlist(String id) {
		super("��ٱ���");// super�� ������ ȣ��
		this.id = id;

		Dimension size = new Dimension(600, 400);
		createpanel();
		createtb();
		tablesetting();
		createchkbox();

		init();

		this.setLocation(300, 300);
		this.setSize(size);
		this.add(tabpane);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

	}

	public void init() {
		initList = dao.getbList(id);
		for (int i = 0; i < initList.size(); i++) {
			tablemodel.addRow(initList.get(i));
		}

	}

	public void tablesetting() {
		table.setRowMargin(0);
		table.getColumnModel().setColumnMargin(0);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);

		table.setShowVerticalLines(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {// ���콺 ���� Ŭ��
					modIntRow = table.getSelectedRow();

					if (e.getClickCount() == 2) {// ���� ���� Ŭ��

					}
					if (e.getClickCount() == 3) {

						modIntRow = table.getSelectedRow();
					}
				}
			}
		});
		DefaultTableCellRenderer ts = new DefaultTableCellRenderer();
		ts.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tc = table.getColumnModel();
		for (int i = 0; i < tc.getColumnCount(); i++) {
			tc.getColumn(i).setCellRenderer(ts);
		}

	}

	private void createtb() {
		south_north.setLayout(new BoxLayout(south_north, BoxLayout.X_AXIS));
		JLabel all = new JLabel("�ֹ��� �ݾ�");
		south_north.add(all);

		south_north.add(total);

		JButton modB = new JButton("�ֹ��ϱ�");
		south_north.add(modB);
		modB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String in[] = new String[5];
				modIntRow = -1;
				makesDTO(id, goodsList, chk);
				gotoInsert(dto);
				JOptionPane.showMessageDialog(null, "�ֹ��� �Ϸ�Ǿ����ϴ�.");
				new OrderList(id);
				dispose();
			}
		});

	}

	private void createchkbox() {
		JCheckBox box = new JCheckBox();
		box.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn("üũ").setCellEditor(new DefaultCellEditor(box));
		box.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				box.setBorderPainted(true);
				box.setHorizontalAlignment(JLabel.CENTER);
				if (e.getStateChange() == ItemEvent.SELECTED) {
					modIntRow = table.getSelectedRow();
					String in[] = new String[6];
					for (int i = 0; i < table.getColumnCount() - 1; i++) {
						in[i] = (String) table.getValueAt(table.getSelectedRow(), i);
						chk = 2;
					}
					in[4] = String.valueOf(table.getSelectedRow());

					if (chk == 2) {
						String[] sarray = new String[6];
						for (int i = 0; i < table.getColumnCount(); i++) {
							String g = String.valueOf(table.getValueAt(table.getSelectedRow(), i));
							sarray[i] = g;
						}
						goodsList.add(sarray);

						for (String[] a : goodsList) {
							for (int i = 0; i < a.length; i++) {
								System.out.println("sarray" + "[" + i + "]" + a[i]);
							}

						}
					int sum = Integer.parseInt(in[2]) * Integer.parseInt(in[3]);
						in[4] = String.valueOf(sum);
						delTableRow(modIntRow);
						tablemodel.insertRow(modIntRow, in);
						int all = Integer.parseInt(in[4]);
						int finalall = 0;
							finalall = finalall + all;
						System.out.println(finalall);
						
						total.setText(String.valueOf(finalall));

					} else {
						chk = 1;
					}
				}
			}

			private void delTableRow(int modIntRow) {
				// TODO Auto-generated method stub
				
			}

		});
	}

	private void makesDTO(String id, ArrayList<String[]> goodsList, int chk) {
		for (int j = 0; j < goodsList.size(); j++) {
			dto = new BasketlistDTO();
			dto.setId(id);
			int code = Integer.parseInt(goodsList.get(j)[0]);
			dto.setCode(code);
			dto.setCname(goodsList.get(j)[1]);
			int cnt = Integer.parseInt(goodsList.get(j)[2]);
			dto.setCnt(cnt);
			int price = Integer.parseInt(goodsList.get(j)[3]);
			dto.setPrice(price);
			dto.setCheck(chk);
			rlist.add(dto);
		}
		for (int j = 0; j < rlist.size(); j++) {
			System.out.println("----------------------------");
			System.out.println("rlist: " + rlist.get(j).getId());
		}

	}

	private void gotoInsert(BasketlistDTO dto) {
		for (int j = 0; j < rlist.size(); j++) {
			dao.Insert(rlist.get(j));
		}

	}

	private void createpanel() {
		this.add(tab_center, "Center");
		this.add(tab_south, "South");

		tab_center.setLayout(new BorderLayout());
		tab_center.add(tableScroll, "Center");
		tab_center.add(tab_south, "South");
		tabpane.add("shopping", tab_center);

		tab_south.setLayout(new BorderLayout());
		tab_south.add(south_north, "North");

	}

}





