package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import discs.Disc;
import discs.DiscColor;
import model.ReadOnlyReversiModel;
import model.ReversiHexModel;
import model.ReversiModel;

public class ReversiGUI extends JFrame implements ReversiView {
  private final ReadOnlyReversiModel model;
  private final JButton[][] boardButtons;
  private int prevX = - 1;
  private int prevY = - 1;

  public ReversiGUI(ReadOnlyReversiModel model) {
    this.model = model;

    getContentPane().setBackground(Color.DARK_GRAY);
    setTitle(model.getType() + " Reversi");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    int dimensions = model.getDimensions();
    boardButtons = new JButton[dimensions][dimensions];

    int side = 50;
    int hexWidth = 2 * side;
    int hexHeight = (int) (Math.sqrt(3) * side);
    int verticalOverlap = side / 2;
    for (int i = 0; i < dimensions; i++) {
      int xOffsetAdjustment = 0;
      if (i % 2 == 1) {
        int horizontalOffset = side / 4;
        xOffsetAdjustment = -horizontalOffset;
      }

      int yOffset = i * (hexHeight - verticalOverlap);
      for (int j = 0; j < dimensions; j++) {
        int xOffset = j * (hexWidth - (side / 2)) + (i % 2) * (hexWidth / 2) + xOffsetAdjustment;
        boardButtons[i][j] = new JButton();
        boardButtons[i][j].setOpaque(false);
        boardButtons[i][j].setContentAreaFilled(false);
        boardButtons[i][j].setBorderPainted(false);
        boardButtons[i][j].setFocusPainted(false);
        boardButtons[i][j].setBounds(xOffset, yOffset, hexWidth, hexHeight);
        int actualI = i;
        int actualJ = j;
        boardButtons[i][j].addMouseListener(new MouseListener() {

          @Override
          public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            System.out.println("Disc position at " + "(" + actualJ + ", " + actualI + ")");
            Icon icon = button.getIcon();

            if(!model.checkValidCoordinates(actualJ, actualI) && !(prevX == - 1 && prevY == - 1)) {
              DiscColor originalColor = model.getDiscAt(prevX, prevY).getColor();
              switch (originalColor) {
                case WHITE:
                  boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.WHITE));
                  break;
                case BLACK:
                  boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.BLACK));
                  break;
                case FACEDOWN:
                  boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.GRAY));

                  break;
                default:
              }
              prevX = - 1;
              prevY = - 1;
            }


            if (icon instanceof ImageIcon) {
              BufferedImage image = convertIconToBufferedImage((ImageIcon) icon);

              int x = image.getWidth() / 2;
              int y = image.getHeight() / 2;
              int rgb = image.getRGB(x, y);
              Color color = new Color(rgb, true);

              if (!model.isDiscFlipped(actualJ, actualI)) {
                if (prevX == - 1 && prevY == - 1) {
                  prevX = actualJ;
                  prevY = actualI;
                  button.setIcon(newHexagonIcon(Color.CYAN));
                } else {
                  DiscColor originalColor = model.getDiscAt(prevX, prevY).getColor();
                  switch (originalColor) {
                    case WHITE:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.WHITE));
                      break;
                    case BLACK:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.BLACK));
                      break;
                    case FACEDOWN:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.GRAY));

                      break;
                    default:
                  }
                  button.setIcon(newHexagonIcon(Color.CYAN));
                  prevX = actualJ;
                  prevY = actualI;
                }
              } else {
                if (!(prevX == -1 && prevY == -1)) {
                  DiscColor originalColor = model.getDiscAt(prevX, prevY).getColor();
                  switch (originalColor) {
                    case WHITE:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.WHITE));
                      break;
                    case BLACK:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.BLACK));
                      break;
                    case FACEDOWN:
                      boardButtons[prevY][prevX].setIcon(newHexagonIcon(Color.GRAY));

                      break;
                    default:
                  }
                  prevX = - 1;
                  prevY = - 1;
                }
              }

              if(color.equals(Color.CYAN)) {
                DiscColor originalColor = model.getDiscAt(actualJ, actualI).getColor();
                switch(originalColor) {
                  case WHITE:
                    button.setIcon(newHexagonIcon(Color.WHITE));
                    break;
                  case BLACK:
                    button.setIcon(newHexagonIcon(Color.BLACK));
                    break;
                  case FACEDOWN:
                    button.setIcon(newHexagonIcon(Color.GRAY));

                    break;
                  default:
                }
                prevX = - 1;
                prevY = - 1;
              }
            }
          }

          @Override
          public void mouseClicked(MouseEvent e) {

          }

          @Override
          public void mouseReleased(MouseEvent e) {

          }


          @Override
          public void mouseEntered(MouseEvent e) {

          }

          @Override
          public void mouseExited(MouseEvent e) {

          }

        });
        add(boardButtons[i][j]);
      }
    }

    int windowWidth = dimensions * hexWidth - (dimensions / 2 - 1) * (hexWidth / 2);
    int windowHeight = (dimensions - 1)
            * (hexHeight - verticalOverlap)
            + hexHeight + (hexHeight / 2);
    setSize(windowWidth, windowHeight);
    setVisible(true);
    render();
  }

  public void render() {
    Disc[][] board = model.getCurrentBoardState();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        JButton button = boardButtons[i][j];
        ImageIcon icon = null;
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setVisible(true);
        if (board[i][j] != null) {
          if (board[i][j].getColor() == DiscColor.WHITE) {
            icon = newHexagonIcon(Color.WHITE);
          } else if (board[i][j].getColor() == DiscColor.BLACK) {
            icon = newHexagonIcon(Color.BLACK);
          } else if (board[i][j].getColor() == DiscColor.FACEDOWN) {
            icon = newHexagonIcon(Color.GRAY);
          }
          button.setIcon(icon);
        }
      }
    }
  }

  private BufferedImage convertIconToBufferedImage(ImageIcon icon) {
    Image image = icon.getImage();
    BufferedImage buffImage = new BufferedImage(image.getWidth(null),
            image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    Graphics2D buffGraphics = buffImage.createGraphics();
    buffGraphics.drawImage(image, 0, 0, null);
    buffGraphics.dispose();
    return buffImage;
  }

  private ImageIcon newHexagonIcon(Color color) {
    BufferedImage hexImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = hexImage.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int r = 40;
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    double startAngle = Math.PI / 6;
    for (int i = 0; i < 6; i++) {
      xPoints[i] = (int) (50 + r * Math.cos((i * 2 * Math.PI / 6) + startAngle));
      yPoints[i] = (int) (50 + r * Math.sin((i * 2 * Math.PI / 6) + startAngle));
    }

    g2.setComposite(AlphaComposite.Clear);
    g2.fillRect(0, 0, 100, 100);

    g2.setComposite(AlphaComposite.Src);
    g2.setColor(color);
    g2.fillPolygon(xPoints, yPoints, 6);

    g2.setColor(Color.BLACK);
    g2.drawPolygon(xPoints, yPoints, 6);

    return new ImageIcon(hexImage);
  }

  public static void main(String[] args) {
    ReversiModel model = new ReversiHexModel();
    model.startGame(7);
    model.makeMove(2,2);
//    model.makeMove(5,2);
//    model.makeMove(6,2);
//    model.pass();
//    model.makeMove(5, 4);

    ReversiGUI gui = new ReversiGUI(model);
    gui.render();
  }
}