package ro.kmagic.utils.profile;


import ro.kmagic.Main;
import ro.kmagic.types.YunoUser;
import ro.kmagic.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProfileImage {

    private final YunoUser user;

    public ProfileImage(YunoUser user) {
        this.user = user;
    }

    public File getProfileImage() throws IOException {
        Font nameFont = new Font("Josefin Sans Bold", Font.PLAIN, (int) (65*1.4));
        Font tagFont = new Font("Josefin Sans Regular", Font.PLAIN, (int) (36*1.4));
        Font levelFont = new Font("Josefin Sans Regular", Font.PLAIN, (int) (29*1.4));
        Font expFont = new Font("Josefin Sans Regular", Font.PLAIN, (int) (29*1.4));
        Font coinsFont = new Font("Josefin Sans Regular", Font.PLAIN, (int) (32*1.4));

        BufferedImage result = new BufferedImage(1840, 712, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        BufferedImage profileImg = Utils.getUserAvatar(user.getJdaUser());
        BufferedImage backgroundImage = ImageIO.read(Main.class.getResource("/images/background.png"));

        // progress bar
        int progressWidth = 919;

        int level = user.getLevel();
        int exp = user.getExperience();
        int nextExp = 25 * level * (1 + level);

        float percent = (float) exp / nextExp;

        // draw bg
        g.drawImage(backgroundImage, 0, 0, 1840, 712, 0, 0, 1840, 712, null);

        // draw avatar
        g.setClip(new Ellipse2D.Float(158, 68, 390, 390));
        g.drawImage(profileImg, 158, 68, 390, 390, null);
        g.setClip(null);

        // draw exp progress
        g.setColor(Color.WHITE);

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g.fill(new RoundRectangle2D.Float(770, 395, (percent * progressWidth), 74, 75, 77));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // add username & tag
        GFX.addText(user.getJdaUser().getName(), nameFont, 640, 200, g, Color.white);
        GFX.addText("#" + user.getJdaUser().getDiscriminator(), tagFont, 640, 250, g, Color.white);

        // add EXP text
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.78f));
        GFX.addCenterText(exp + " / " + nextExp, expFont, 1235, 445, g, Color.WHITE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // add LEVEL text
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
        GFX.addText("Level " + level, levelFont, 1555, 515, g, Color.WHITE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // add COINS text
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        GFX.addText(user.getCoins() + " coins", coinsFont, 750, 600, g, Color.WHITE);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));


        File file = new File("profile_" + user.getId() + ".png");
        ImageIO.write(result, "png", file);

        return file;
    }

}
