package com.cosin.controller;

import cn.example.framework.controller.*;
import cn.example.application.service.impl.*;
import org.springframework.beans.factory.annotation.*;
import cn.example.application.service.*;
import org.slf4j.*;
import cn.example.framework.util.yml.*;
import javax.servlet.http.*;
import cn.example.framework.util.user.*;
import org.apache.shiro.*;
import org.apache.shiro.authc.*;
import cn.example.framework.exception.*;
import org.apache.shiro.subject.*;
import org.apache.shiro.session.*;
import org.apache.shiro.session.mgt.eis.*;
import cn.example.framework.util.uuid.*;
import eu.bitwalker.useragentutils.*;
import org.springframework.web.multipart.*;
import java.io.*;
import org.springframework.web.bind.annotation.*;
import cn.example.application.entity.user.*;
import org.apache.shiro.authz.annotation.*;
import cn.example.framework.util.encrypt.*;
import java.util.*;

@RestController
public class LoginController extends BaseController
{
    private final Logger logger;
    @Autowired
    private SliverMailServiceImpl a;
    @Autowired
    private SystemUserService service;
    @Autowired
    private SocketIoService b;
    private SystemLog c;
    private SystemUser d;
    private String e;
    private String f;
    private String USER_ID;
    private String g;
    private String USER_AGENT;
    private String h;
    private String i;
    private String j;
    private String k;
    private String PATH;
    private Long l;
    private Long m;

    public LoginController() {
        this.logger = LoggerFactory.getLogger(this.getClass().getName());
        this.c = new SystemLog();
        this.d = new SystemUser();
        this.e = "LAST_LOGON";
        this.f = "USER_KEY";
        this.USER_ID = "USER_ID";
        this.g = "token";
        this.USER_AGENT = "User-Agent";
        this.h = "LOGIN";
        this.i = "LOGOUT";
        this.j = "KICK";
        this.k = "SLIVERWORKSPACE_CONCURRENT_LOGON";
        this.PATH = cn.example.framework.util.yml.a.get("example.conf");
        this.l = 100000001L;
        this.m = 100000000L;
    }

    @RequestMapping({ "/doLogin" })
    public Map<String, Object> a(final SystemUser user, final HttpServletRequest request) {
        final Map<String, String> info = cn.example.framework.util.user.b.getLicenceInfo();
        if (!cn.example.framework.util.user.b.e(info)) {
            this.a.a((Map)info);
            return (Map<String, Object>)cn.example.framework.util.controller.b.M();
        }
        final String userName = user.getUserName();
        if (null == userName) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.userNameNotBlank", new Object[0]);
        }
        final UsernamePasswordToken token = new UsernamePasswordToken(userName, user.getUserPass());
        final Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login((AuthenticationToken)token);
        }
        catch (UnknownAccountException uae) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.unknownAccount", new Object[0]);
        }
        catch (IncorrectCredentialsException ice) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.passwordIncorrect", new Object[0]);
        }
        catch (LockedAccountException lae) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.accountLocked", new Object[0]);
        }
        catch (ExcessiveAttemptsException eae) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.accountLockedTimes", new Object[0]);
        }
        catch (DisabledAccountException sae) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.accountDisabled", new Object[0]);
        }
        catch (AuthenticationException ae) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.accountAndPassIncorrect", new Object[0]);
        }
        final Date current = new Date();
        if (!currentUser.isAuthenticated()) {
            token.clear();
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.failure", new Object[0]);
        }
        final Session session = currentUser.getSession();
        final SystemUser tUser = this.service.w(userName);
        if (cn.example.framework.util.user.b.h(info) && !tUser.getUserId().equals(this.l)) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("login.personalLoginError", new Object[0]);
        }
        try {
            this.d.reset();
            this.d.setUserId(tUser.getUserId());
            this.d.setUpdateTime(current);
            final int num = this.service.updateUser(this.d);
            if (num > 0) {
                tUser.setUpdateTime(current);
            }
        }
        catch (ServiceException e) {
            this.logger.error(e.getMessage());
        }
        final String sessionID = String.valueOf(session.getId());
        this.logger.info("SESSION Timeout:{}", (Object)session.getTimeout());
        if (this.a(userName, sessionID)) {
            this.a(request, tUser.getUserId(), current, this.j);
        }
        final List<String> roleNames = (List<String>)this.service.listRoleNameByUserId(tUser.getUserId());
        final Map<String, Object> result = this.d(tUser);
        result.put(this.g, session.getId());
        result.put("roleNames", roleNames);
        this.service.e(sessionID, Long.valueOf(current.getTime()));
        session.setAttribute((Object)this.g, (Object)sessionID);
        session.setAttribute((Object)this.USER_ID, (Object)tUser.getUserId());
        session.setAttribute((Object)this.f, (Object)tUser.getUserName());
        session.setAttribute((Object)this.e, (Object)tUser.getUpdateTime());
        this.a(request, tUser.getUserId(), current, this.h);
        return (Map<String, Object>)cn.example.framework.util.controller.b.b("login.success", new Object[] { result });
    }

    @RequestMapping({ "/doLogout" })
    public Map<String, Object> a(final HttpServletRequest request) {
        final Date current = new Date();
        final String sessionId = String.valueOf(SecurityUtils.getSubject().getSession().getId());
        final Long userId = (Long)SecurityUtils.getSubject().getSession().getAttribute((Object)this.USER_ID);
        this.service.A(sessionId);
        this.a(request, userId, current, this.i);
        SecurityUtils.getSubject().logout();
        return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[0]);
    }

    private boolean a(final String username, final String sessionID) {
        this.logger.info("validateSiglerLogon  Username : {} SessionID : {}", (Object)username, (Object)sessionID);
        boolean result = false;
        final EnterpriseCacheSessionDAO sessionDAO = (EnterpriseCacheSessionDAO)cn.example.framework.util.context.a.R("sessionDao");
        final Collection<Session> sessions = (Collection<Session>)sessionDAO.getActiveSessions();
        for (final Session session : sessions) {
            final String loginUsername = (String)session.getAttribute((Object)this.f);
            final String loginSessionId = (String)session.getAttribute((Object)this.g);
            if (username.equals(loginUsername) && !loginSessionId.equals(sessionID)) {
                sessionDAO.delete(session);
                final String sid = String.valueOf(session.getId());
                final String uuid = this.service.D(sid);
                if (null == uuid) {
                    continue;
                }
                this.b.a(uuid, "error", this.k);
                this.service.A(sid);
                result = true;
            }
        }
        return result;
    }

    private void a(final HttpServletRequest request, final Long userId, final Date current, final String type) {
        this.c.reset();
        final String ua = request.getHeader(this.USER_AGENT);
        final UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        final Browser browser = userAgent.getBrowser();
        final OperatingSystem os = userAgent.getOperatingSystem();
        this.c.setLogId(Long.valueOf(IDUtil.id()));
        this.c.setUserId(userId);
        this.c.setLogTime(current);
        this.c.setLogType(type);
        final String ip = cn.example.framework.util.ip.a.b(request);
        if (null != ip) {
            this.c.setLogIp(ip);
            final String local = cn.example.framework.util.ip.a.T(ip);
            this.c.setLogLocal(local);
        }
        this.c.setOsName(os.getName());
        if (null != os.getDeviceType()) {
            this.c.setOsDevice(os.getDeviceType().getName());
        }
        this.c.setBrowserName(browser.getName());
        if (null != browser.getRenderingEngine()) {
            this.c.setBrowserEngine(browser.getRenderingEngine().getName());
        }
        if (null != userAgent.getBrowserVersion()) {
            this.c.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
        }
        try {
            this.service.saveLog(this.c);
            if (type.equals(this.i)) {
                this.logger.info("logout success");
            }
            else if (type.equals(this.h)) {
                this.logger.info("login  success");
            }
            else if (type.equals(this.j)) {
                this.logger.info("logout kick");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error(e.getMessage());
        }
    }

    @RequestMapping({ "/code" })
    public Map<String, Object> a() {
        return (Map<String, Object>)cn.example.framework.util.controller.b.b("SUCCESS", new Object[] { cn.example.framework.util.user.b.getLicenceInfo() });
    }

    @ResponseBody
    @RequestMapping({ "/active" })
    public Map<String, Object> a(@RequestParam(name = "file", required = true) final MultipartFile file) {
        Map<String, String> result = null;
        InputStream in = null;
        try {
            if (file.getSize() > 5120L) {
                return (Map<String, Object>)cn.example.framework.util.controller.b.c("error.licenceFileOverSize", new Object[0]);
            }
            if (!file.getOriginalFilename().equals("test.licence")) {
                return (Map<String, Object>)cn.example.framework.util.controller.b.c("error.licenceName", new Object[0]);
            }
            in = file.getInputStream();
            final FileOutputStream out = new FileOutputStream(this.PATH + File.separator + "test.licence");
            final int len = in.available();
            final byte[] buffer = new byte[len];
            in.read(buffer);
            out.write(buffer);
            out.close();
            result = cn.example.framework.util.user.b.getLicenceInfo();
            final Map<String, String> info = cn.example.framework.util.user.b.R();
            if (null == info) {
                return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { result });
            }
            this.a.a((Map)info);
            final String code = result.get("code");
            if (null == code || "".equals(code)) {
                result.put("code", cn.example.framework.util.user.b.getMachineCode());
            }
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("Failure", new Object[] { result });
        }
        catch (Exception e) {
            e.printStackTrace();
            this.log.error(e.getMessage());
            return (Map<String, Object>)cn.example.framework.util.controller.b.c("error.internalError", new Object[0]);
        }
        finally {
            if (null != in) {
                try {
                    in.close();
                }
                catch (IOException ex) {}
            }
        }
    }

    @RequestMapping({ "/unauthorized" })
    public Map<String, Object> b() {
        this.logger.info("error.notAuthorization");
        return (Map<String, Object>)cn.example.framework.util.controller.b.b();
    }

    @RequestMapping({ "/login" })
    public Map<String, Object> c() {
        return (Map<String, Object>)cn.example.framework.util.controller.b.c("NOTLOGIN", new Object[0]);
    }

    @RequestMapping({ "/menu" })
    @RequiresPermissions({ "/menu" })
    public Map<String, Object> d() {
        final Subject currentUser = SecurityUtils.getSubject();
        final Session session = currentUser.getSession();
        final Long userId = (Long)session.getAttribute((Object)this.USER_ID);
        if (null != userId) {
            List<SystemPerm> list = (List<SystemPerm>)this.service.listViewedPermByUserId(userId);
            list = cn.example.framework.util.user.b.b(list);
            if (null != list && list.size() > 0) {
                for (int z = 0; z < list.size(); ++z) {
                    this.a(list.get(z));
                }
            }
            return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { list });
        }
        return (Map<String, Object>)cn.example.framework.util.controller.b.b(new Object[0]);
    }

    @RequestMapping({ "/profile" })
    @RequiresPermissions({ "/profile" })
    public Map<String, Object> e() {
        final Subject currentUser = SecurityUtils.getSubject();
        final Session session = currentUser.getSession();
        final Long userId = (Long)session.getAttribute((Object)this.USER_ID);
        if (null != userId) {
            final SystemUser result = this.service.aQ(userId);
            result.setDisabled((Boolean)null);
            result.setUserName((String)null);
            result.setUpdateTime((Date)null);
            result.setCreateTime((Date)null);
            result.setUserTheme((String)null);
            return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { result });
        }
        return (Map<String, Object>)cn.example.framework.util.controller.b.b(new Object[0]);
    }

    @RequestMapping({ "/profileSave" })
    @RequiresPermissions({ "/profileSave" })
    public Map<String, Object> a(final SystemUser user) {
        final Subject currentUser = SecurityUtils.getSubject();
        final Session session = currentUser.getSession();
        final Long userId = (Long)session.getAttribute((Object)this.USER_ID);
        final String userName = (String)session.getAttribute((Object)this.f);
        this.log.info("userId:{} userName:{}", (Object)userId, (Object)userName);
        if (null != userId && null != userName && !"".equals(userName)) {
            try {
                if (null != user.getUserPass() && !"".equals(user.getUserPass()) && null != user.getRepassword() && !"".equals(user.getRepassword()) && user.getUserPass().equals(user.getRepassword())) {
                    if (!user.getUserPass().equals(user.getRepassword())) {
                        return (Map<String, Object>)cn.example.framework.util.controller.b.c("profile.passNotEqual", new Object[0]);
                    }
                    final SystemUser temp = this.service.w(userName);
                    if (null != temp) {
                        final String old_salt = temp.getUserSalt();
                        final String old_pass = EncryptUtil.n(user.getOdpassword(), old_salt);
                        if (!old_pass.equals(temp.getUserPass())) {
                            return (Map<String, Object>)cn.example.framework.util.controller.b.c("profile.passNotCorrect", new Object[0]);
                        }
                        user.setUserId(userId);
                        user.setUserName(userName);
                        final String salt = EncryptUtil.P();
                        final String pass = EncryptUtil.n(user.getUserPass(), salt);
                        user.setUserSalt(salt);
                        user.setUserPass(pass);
                        user.setCreateTime((Date)null);
                        user.setDisabled((Boolean)null);
                        user.setUpdateTime(new Date());
                        final int result = this.service.saveUser(user);
                        if (result > 0) {
                            return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { result });
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return (Map<String, Object>)cn.example.framework.util.controller.b.J();
            }
        }
        return (Map<String, Object>)cn.example.framework.util.controller.b.c("profile.updateError", new Object[0]);
    }

    @RequestMapping({ "/loadUser" })
    @RequiresPermissions({ "/loadUser" })
    public Map<String, Object> b(final SystemUser user) {
        final String userName = user.getUserName();
        if (null == userName) {
            return (Map<String, Object>)cn.example.framework.util.controller.b.b(new Object[0]);
        }
        final Subject currentUser = SecurityUtils.getSubject();
        final Session session = currentUser.getSession();
        final Date lastLogon = (Date)session.getAttribute((Object)this.e);
        if (null != lastLogon) {
            final SystemUser tUser = this.service.w(userName);
            final List<String> roleNames = (List<String>)this.service.listRoleNameByUserId(tUser.getUserId());
            final Map<String, Object> result = this.d(tUser);
            result.put(this.g, session.getId());
            result.put("roleNames", roleNames);
            return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { result });
        }
        return (Map<String, Object>)cn.example.framework.util.controller.b.b(new Object[0]);
    }

    @RequestMapping({ "/doUpdate" })
    @RequiresPermissions({ "/doUpdate" })
    public Map<String, Object> c(final SystemUser user) {
        this.logger.info("user will be updated.");
        final int num = this.service.updateUser(user);
        return (Map<String, Object>)cn.example.framework.util.controller.b.a(new Object[] { num });
    }

    private Map<String, Object> d(final SystemUser user) {
        final Map<String, Object> result = new HashMap<String, Object>();
        result.put("userId", user.getUserId());
        result.put("userName", user.getUserName());
        result.put("userEmail", user.getUserEmail());
        result.put("userTheme", user.getUserTheme());
        result.put("updateTime", user.getUpdateTime());
        return result;
    }

    private void a(final SystemPerm sp) {
        sp.setUserId((Long)null);
        sp.setApi((Boolean)null);
        sp.setId((Long)null);
        sp.setPid((Long)null);
        sp.setChecked((Boolean)null);
        sp.setExpand((Boolean)null);
        final List<SystemPerm> children = (List<SystemPerm>)sp.getChildren();
        if (null != children && children.size() > 0) {
            for (int i = 0; i < children.size(); ++i) {
                this.a(children.get(i));
            }
        }
    }
}
