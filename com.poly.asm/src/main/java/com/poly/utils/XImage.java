/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.utils;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author Zander
 */
public class XImage {
    public static Image getAppIcon() {
        URL url = XImage.class.getResource("/com/poly/icon/fpt.png");
        return new ImageIcon(url).getImage();
    }
    public static void save(File src) {
        File dst = new File("src\\main\\resources\\com\\poly\\icon", src.getName());
        if (!dst.getParentFile().exists()) {
            dst.getParentFile().mkdirs(); 
        }
        try {
            Path from = Paths.get(src.getAbsolutePath());
            Path to = Paths.get(dst.getAbsolutePath());
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ImageIcon read(String filename) {
        File path = new File("src\\main\\resources\\com\\poly\\icon", filename);
        return new ImageIcon(new ImageIcon(path.getAbsolutePath()).getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));
    }
}
