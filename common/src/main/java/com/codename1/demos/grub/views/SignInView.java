/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Codename One through http://www.codenameone.com/ if you
 * need additional information or have any questions.
 */

package com.codename1.demos.grub.views;

import com.codename1.components.ScaleImageLabel;
import com.codename1.demos.grub.interfaces.Account;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.Node;
import com.codename1.rad.ui.AbstractEntityView;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.validation.LengthConstraint;
import com.codename1.ui.validation.RegexConstraint;
import com.codename1.ui.validation.Validator;

import static com.codename1.ui.CN.convertToPixels;
import static com.codename1.ui.util.Resources.getGlobalResources;

public class SignInView<T extends Entity> extends AbstractEntityView<T> {

    public static final ActionNode.Category COMPLETE_SIGNING_IN = new ActionNode.Category();

    public SignInView(T entity, Node applicationControllerViewNode, Node accountViewNode) {
        super(entity);
        setUIID("SignInCnt");

        Container wrapper = new Container(new BorderLayout());
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollVisible(false);
        setScrollableY(true);
        Label signInHeader = new Label("Welcome Back!", "SignInHeader");
        Image grubLogo = getGlobalResources().getImage("grub-logo.png");
        ScaleImageLabel logoLabel = new ScaleImageLabel(grubLogo);
        Container signInTopView = BoxLayout.encloseY(logoLabel, signInHeader);
        signInTopView.setUIID("SignInTopView");
        wrapper.add(BorderLayout.NORTH, signInTopView);

        TextField emailAddress = new TextField("", "Email Address", 20, TextArea.EMAILADDR);
        emailAddress.setUIID("SignInField");
        emailAddress.getHintLabel().setUIID("SignInFieldHint");

        TextField password = new TextField("", "Password", 20, TextArea.PASSWORD);
        password.setUIID("SignInField");
        password.getHintLabel().setUIID("SignInFieldHint");

        Validator validator = new Validator();
        validator.addConstraint(emailAddress, RegexConstraint.validEmail());
        validator.addConstraint(password, new LengthConstraint(8));

        wrapper.add(BorderLayout.CENTER, BoxLayout.encloseY(emailAddress, password));

        Button confirmSignIn = new Button("Sign In", "SignInConfirmButton");
        validator.addSubmitButtons(confirmSignIn);
        confirmSignIn.addActionListener(evt -> {
            evt.consume();
            entity.set(Account.firstName, "Codename");
            entity.set(Account.lastName, "One");
            entity.set(Account.emailAddress, emailAddress.getText());
            entity.set(Account.password, password.getText());
            entity.set(Account.mobileNumber, "0542424242");

            ActionNode action = applicationControllerViewNode.getInheritedAction(COMPLETE_SIGNING_IN);
            if (action != null) {
                action.fireEvent(entity, SignInView.this);
            }
        });

        Label goToSignUpLabel = new Label("DON'T HAVE AN ACCOUNT?", "SignInLabel");
        Button goToSignUpButton = new Button("SIGN UP", "GoToSighUpButton");

        goToSignUpButton.addActionListener(evt->{
            evt.consume();
            ActionNode action = accountViewNode.getInheritedAction(AccountView.SIGN_UP);
            if (action != null) {
                action.fireEvent(entity, SignInView.this);
            }
        });

        Label continueWith = new Label("----OR CONTINUE WITH----", "SignInLabel");
        Button forgotPassword = new Button("FORGOT PASSWORD?", "SignInLabel");

        Image facebookIconImage = getGlobalResources().getImage("facebook-icon.png").scaled(convertToPixels(5), convertToPixels(5));
        Image googleIconImage = getGlobalResources().getImage("google-icon.png").scaled(convertToPixels(5), convertToPixels(5));
        Image appleIconImage = getGlobalResources().getImage("apple-icon.png").scaled(convertToPixels(5), convertToPixels(5));

        Button faceBookIconButton = new Button(facebookIconImage);
        Button googleIconButton = new Button(googleIconImage);
        Button appleIconButton = new Button(appleIconImage);

        Container signInOptionsCnt = BoxLayout.encloseY(confirmSignIn,
                FlowLayout.encloseCenter(goToSignUpLabel, goToSignUpButton),
                continueWith,
                FlowLayout.encloseCenter(faceBookIconButton, googleIconButton, appleIconButton),
                forgotPassword
        );
        signInOptionsCnt.setUIID("SignOptionsCnt");
        wrapper.add(BorderLayout.SOUTH, signInOptionsCnt);

        if (CN.isTablet()){
            setLayout(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE));
            add(BorderLayout.CENTER, wrapper);
        }else{
            add(wrapper);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void commit() {

    }

    @Override
    public Node getViewNode() {
        return null;
    }
}
