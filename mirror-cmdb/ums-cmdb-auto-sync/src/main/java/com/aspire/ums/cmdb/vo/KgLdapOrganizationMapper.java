package com.aspire.ums.cmdb.vo;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class KgLdapOrganizationMapper implements AttributesMapper<KgLdapOrganization> {
    public static final String O = "o";
    public static final String PARENTORGID = "parentOrgId";
    public static final String DISPLAYORDER = "displayOrder";
    public static final String DISPLAYNAME = "displayName";
    public static final String INITIALS = "initials";
    public static final String ENTRYUUID = "entryUUID";
    public static final String DESCRIPTION = "description";
    @Override
    public KgLdapOrganization mapFromAttributes(Attributes attributes) throws NamingException {
        KgLdapOrganization organization = new KgLdapOrganization();
        if (attributes == null) {
            return organization;
        }
        organization.setO(attributes.get(O) == null ? null: (String) attributes.get(O).get());
        organization.setParentOrgId(attributes.get(PARENTORGID) == null ? null: (String) attributes.get(PARENTORGID).get());
        organization.setDisplayOrder(attributes.get(DISPLAYORDER) == null ? null: (String) attributes.get(DISPLAYORDER).get());
        organization.setDisplayName(attributes.get(DISPLAYNAME) == null ? null: (String) attributes.get(DISPLAYNAME).get());
        organization.setInitials(attributes.get(INITIALS) == null ? null: (String) attributes.get(INITIALS).get());
        organization.setEntryUUID(attributes.get(ENTRYUUID) == null ? null: (String) attributes.get(ENTRYUUID).get());
        organization.setDescription(attributes.get(DESCRIPTION) == null ? null: (String) attributes.get(DESCRIPTION).get());
        return organization;
    }
}
