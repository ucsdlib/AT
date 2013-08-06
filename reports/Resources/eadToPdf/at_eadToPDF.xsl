<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:ead="urn:isbn:1-931666-22-9" xmlns:ns2="http://www.w3.org/1999/xlink"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!--
        *******************************************************************
        *                                                                 *
        * VERSION:          1.02                                           *
        *                                                                 *
        * AUTHOR:           Winona Salesky                                *
        *                   wsalesky@gmail.com                            *
        *                                                                 *
        *                                                                 *
        * ABOUT:           This file has been created for use with        *
        *                  the Archivists' Toolkit  July 30 2008.         *
        *                  this file calls lookupLists.xsl, which         *
        *                  should be located in the same folder.          *
        *                                                                 *
        * UPDATED          May 31, 2012                                   *
        *                  Fixed bug with multiple instance display       *
        * UPDATED          March 23, 2009                                 *
        *                  Added revision description and date            *
        * UPDATED          March 4, 2009                                  *
        *                  Updated titleproper and page header displays   *
        *                  March 1, 2009                                  *
        *                  Fixed non-wraping long titles                  *
        *                  Feb. 6, 2009                                   *
        *                  Added roles to creator display in summary      * 
        *******************************************************************
    -->
    <xsl:output method="xml" encoding="utf-8" indent="yes"/>
    <xsl:strip-space elements="*"/>
<!--     <xsl:include href="lookupListsPDF.xsl"/>-->
    <xsl:include href="reports/Resources/eadToPdf/lookupListsPDF.xsl"/>
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="ead:ead">
        <!--The follwoing two variables headerString and pageHeader establish the title of the finding aid and substring long titles for dsiplay in the header -->
        <xsl:variable name="headerString">
            <xsl:choose>
                <xsl:when test="ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper">
                    <xsl:choose>
                        <xsl:when test="starts-with(ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper,ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper/ead:num)">
                            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/>
                        </xsl:when>
                        <xsl:when test="ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper/@type = 'filing'">
                            <xsl:choose>
                                <xsl:when test="count(ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper) &gt; 1">
                                    <xsl:apply-templates select="ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper[not(@type='filing')]"/>            
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/>        
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <xsl:otherwise><xsl:apply-templates select="ead:eadheader/ead:filedesc/ead:titlestmt/ead:titleproper"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise><xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/></xsl:otherwise>                
            </xsl:choose>
        </xsl:variable>
        <xsl:variable name="pageHeader">
            <xsl:value-of select="substring($headerString,1,100)"/><xsl:if test="string-length(normalize-space($headerString)) &gt; 100">...</xsl:if>
        </xsl:variable>
        <!--fo:root establishes the page types and layouts contained in the PDF, the finding aid consists of 4 distinct 
            page types, the cover page, the table of contents, contents and the container list. To alter basic page apperence 
            such as margins fonts alter the following page-masters.-->
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <!-- Page master for Cover Page -->
                <fo:simple-page-master master-name="cover-page" page-width="8.5in"
                    page-height="11in" margin-top="0.2in" margin-bottom="0.5in" margin-left="0.5in" margin-right="0.5in">
                    <fo:region-body margin="0.5in" margin-bottom="1in"/>
                    <fo:region-before extent="0.2in"/>
                    <fo:region-after extent="2in"/>
                </fo:simple-page-master>
                <!-- Page master for Table of Contents -->
                <fo:simple-page-master master-name="toc" page-width="8.5in" page-height="11in"
                    margin-top="0.2in" margin-bottom="0.5in" margin-left="0.5in" margin-right="0.5in">
                    <fo:region-body margin="0.2in" margin-bottom="1in"/>
                    <fo:region-before extent="0.2in"/>
                    <fo:region-after extent="0.2in"/>
                </fo:simple-page-master>
                <!-- Page master for Contents -->
                <fo:simple-page-master master-name="contents" page-width="8.5in" page-height="11in"
                    margin-top="0.2in" margin-bottom="0.5in" margin-left="0.5in" margin-right="0.5in">
                    <fo:region-body margin="0.2in" margin-bottom="1in"/>
                    <fo:region-before extent="0.2in"/>
                    <fo:region-after extent="0.2in"/>
                </fo:simple-page-master>
                <!-- Page master for Container List -->
                <fo:simple-page-master master-name="container-list" page-width="8.5in" page-height="11in"
                    margin-top="0.2in" margin-bottom="0.5in" margin-left="0.5in" margin-right="0.5in">
                    <fo:region-body margin-top="0.75in" margin-bottom="1in" margin-left="0.2in" margin-right="0.2in"/>
                    <fo:region-before extent="0.3in"/>
                    <fo:region-after extent="0.2in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <!-- The fo:page-sequence establishes headers, footers and the body of the page.-->
            <fo:page-sequence master-reference="cover-page">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="center" font-size="12pt">
                            <xsl:apply-templates select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt"/>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-family="Times">
                            <xsl:apply-templates select="ead:eadheader"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
            <fo:page-sequence master-reference="toc">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block color="gray" font-size="8pt" text-align="center" font-family="Times">
                        <xsl:value-of select="$pageHeader"/>
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="center" color="gray" font-size="8pt" font-family="Times">
                        <xsl:text>- Page </xsl:text><fo:page-number/><xsl:text> -</xsl:text>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" font-family="Times">
                    <xsl:call-template name="toc"/>
                </fo:flow>
            </fo:page-sequence>
            <fo:page-sequence master-reference="contents">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block color="gray" font-size="8pt" text-align="center" font-family="Times">
                        <xsl:value-of select="$pageHeader"/>
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="center" color="gray" font-size="8pt" font-family="Times">
                        <xsl:text>- Page </xsl:text><fo:page-number/><xsl:text> -</xsl:text>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" font-family="Times">
                    <xsl:apply-templates select="ead:archdesc"/>
                </fo:flow>
            </fo:page-sequence>
            <xsl:if test="ead:archdesc/ead:dsc/child::*">
                <fo:page-sequence master-reference="container-list">
                    <fo:static-content flow-name="xsl-region-before">
                        <fo:block color="gray" font-size="8pt" text-align="center" font-family="Times">
                            <xsl:value-of select="$pageHeader"/>
                        </fo:block>
                        <fo:retrieve-marker retrieve-class-name="series-title"/>
                    </fo:static-content>
                    <fo:static-content flow-name="xsl-region-after">
                        <fo:block text-align="center" color="gray" font-size="8pt" font-family="Times">
                            <xsl:text>- Page </xsl:text><fo:page-number/><xsl:text> -</xsl:text>
                        </fo:block>
                    </fo:static-content>
                    <fo:flow flow-name="xsl-region-body" font-family="Times">
                        <xsl:apply-templates select="ead:archdesc/ead:dsc"/>
                    </fo:flow>
                </fo:page-sequence>
            </xsl:if>
        </fo:root>
    </xsl:template>
    <!-- EAD Header, this information populates the cover page -->
    <xsl:template match="ead:eadheader">
        <fo:block text-align="center" padding-top=".5in" font-weight="bold"
            line-height="24pt" border-bottom="1pt solid #666" space-after="18pt" 
            padding-bottom="12pt">
            <fo:block font-size="20pt" wrap-option="wrap">
                <xsl:choose>
                    <xsl:when test="ead:filedesc/ead:titlestmt/ead:titleproper">
                        <xsl:choose>
                            <xsl:when test="starts-with(ead:filedesc/ead:titlestmt/ead:titleproper,ead:filedesc/ead:titlestmt/ead:titleproper/ead:num)">
                                <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/>
                            </xsl:when>
                            <xsl:when test="ead:filedesc/ead:titlestmt/ead:titleproper/@type = 'filing'">
                                <xsl:choose>
                                    <xsl:when test="count(ead:filedesc/ead:titlestmt/ead:titleproper) &gt; 1">
                                        <xsl:apply-templates select="ead:filedesc/ead:titlestmt/ead:titleproper[not(@type='filing')]"/>            
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/>        
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:when>
                            <xsl:otherwise><xsl:apply-templates select="ead:filedesc/ead:titlestmt/ead:titleproper"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise><xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header"/></xsl:otherwise>                
                </xsl:choose>
            </fo:block>
            <fo:block font-size="16pt">
                <xsl:apply-templates select="ead:filedesc/ead:titlestmt/ead:subtitle"/>
            </fo:block>
            <fo:block font-size="12pt" >
                <xsl:apply-templates select="ead:filedesc/ead:titlestmt/ead:author"/>
            </fo:block>
            
            <!-- Adds repositry branding device. 
            <xsl:if test="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:p/ead:extref">
                <fo:block>
                    <fo:external-graphic src="{/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt/ead:p/ead:extref/ns2:href}" content-height="100%" content-width="100%"/>
                </fo:block>    
            </xsl:if>-->
                
        </fo:block>
        <fo:block margin="1in" space-before="18pt" font-size="12pt" text-align="center"
            font-weight="normal" line-height="24pt">
            <xsl:apply-templates select="ead:profiledesc"/>
        </fo:block>        
        <fo:block margin="1in" space-before="18pt" font-size="12pt" text-align="center"
            font-weight="normal" line-height="24pt">
            <xsl:apply-templates select="ead:filedesc/ead:editionstmt"/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:filedesc/ead:titlestmt/ead:titleproper/ead:num">
        <fo:block>
            &#160;<xsl:apply-templates/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:publicationstmt">
        <fo:block>
            <xsl:apply-templates select="ead:publisher"/>
        </fo:block>
        <xsl:if test="ead:date">
            <fo:block>
                <xsl:apply-templates select="ead:date"/>
            </fo:block>
        </xsl:if>
        <xsl:apply-templates select="ead:address"/>
    </xsl:template>
    <xsl:template match="ead:address">
        <fo:block>
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:addressline">
        <fo:block line-height="18pt">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:profiledesc/ead:creation/ead:date">  
        <xsl:variable name="month">
            <xsl:choose>
                <xsl:when test="substring(.,6,2) = '01'">January</xsl:when>
                <xsl:when test="substring(.,6,2) = '02'">February</xsl:when>
                <xsl:when test="substring(.,6,2) = '03'">March</xsl:when>
                <xsl:when test="substring(.,6,2) = '04'">April</xsl:when>
                <xsl:when test="substring(.,6,2) = '05'">May</xsl:when>
                <xsl:when test="substring(.,6,2) = '06'">June</xsl:when>
                <xsl:when test="substring(.,6,2) = '07'">July</xsl:when>
                <xsl:when test="substring(.,6,2) = '08'">August</xsl:when>
                <xsl:when test="substring(.,6,2) = '09'">September</xsl:when>
                <xsl:when test="substring(.,6,2) = '10'">October</xsl:when>
                <xsl:when test="substring(.,6,2) = '11'">November</xsl:when>
                <xsl:when test="substring(.,6,2) = '12'">December</xsl:when>
            </xsl:choose>
        </xsl:variable>
        <fo:block line-height="18pt">
            <xsl:value-of select="concat($month,' ',substring(.,9,2),', ',substring(.,1,4))"/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:profiledesc/ead:langusage"/>
    <!-- Special template for header display -->
    <xsl:template match="/ead:ead/ead:archdesc/ead:did/ead:unittitle" mode="header">
        <xsl:apply-templates mode="header"/>
    </xsl:template>
    <xsl:template match="/ead:ead/ead:archdesc/ead:did/ead:unittitle/child::*" mode="header">
        &#160;<xsl:apply-templates/>
    </xsl:template>
    <!-- A named template generating the Table of Contents, order of items is pre-determined, to change the order, rearrange the xsl:if or xsl:for-each statements.  -->
    <xsl:template name="toc">
        <fo:block font-size="16pt" space-before="18pt" space-after="18pt"
            font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000"> Table of Contents </fo:block>
        <fo:block font-size="12pt" line-height="24pt">
            <xsl:if test="/ead:ead/ead:archdesc/ead:did">
                <fo:block text-align-last="justify">
                    <fo:basic-link internal-destination="{generate-id(/ead:ead/ead:archdesc/ead:did)}" text-decoration="underline"> Summary Information </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <fo:page-number-citation ref-id="{generate-id(/ead:ead/ead:archdesc/ead:did)}"/>
                </fo:block>
            </xsl:if>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:bioghist">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Biography/History</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:scopecontent">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Scope and Content</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:arrangement">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Arrangement</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:fileplan">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>File Plan</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>

            <!-- Administrative Information  -->
            <xsl:if
                test="/ead:ead/ead:archdesc/ead:accessrestrict or
                    /ead:ead/ead:archdesc/ead:userestrict or
                    /ead:ead/ead:archdesc/ead:custodhist or
                    /ead:ead/ead:archdesc/ead:accruals or
                    /ead:ead/ead:archdesc/ead:altformavail or
                    /ead:ead/ead:archdesc/ead:acqinfo or
                    /ead:ead/ead:archdesc/ead:processinfo or
                    /ead:ead/ead:archdesc/ead:appraisal or
                    /ead:ead/ead:archdesc/ead:originalsloc">
                <fo:block text-align-last="justify">
                    <fo:basic-link internal-destination="adminInfo" text-decoration="underline">
                        Administrative Information </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <fo:page-number-citation ref-id="adminInfo"/>
                </fo:block>
            </xsl:if>

            <!-- Related Materials -->
            <xsl:if
                test="/ead:ead/ead:archdesc/ead:relatedmaterial or /ead:ead/ead:archdesc/ead:separatedmaterial">
                <fo:block text-align-last="justify">
                    <fo:basic-link internal-destination="relMat" text-decoration="underline">
                        Related Materials </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <fo:page-number-citation ref-id="relMat"/>
                </fo:block>
            </xsl:if>

            <xsl:for-each select="/ead:ead/ead:archdesc/ead:controlaccess">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Controlled Access Headings</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:otherfindaid">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Other Finding Aids</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:phystech">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Technical Requirements</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:odd">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Other Descriptive Data</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:bibliography">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Bibliography</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:index">
                <fo:block text-align-last="justify">
                    <fo:basic-link text-decoration="underline">
                        <xsl:call-template name="tocLinks"/>
                        <xsl:choose>
                            <xsl:when test="ead:head">
                                <xsl:value-of select="ead:head"/>
                            </xsl:when>
                            <xsl:otherwise>Index</xsl:otherwise>
                        </xsl:choose>
                    </fo:basic-link>
                    <xsl:text/>
                    <fo:leader leader-pattern="dots"/>
                    <xsl:text/>
                    <xsl:call-template name="tocPage"/>
                </fo:block>
            </xsl:for-each>
            <xsl:for-each select="/ead:ead/ead:archdesc/ead:dsc">
                <xsl:if test="child::*">
                    <fo:block text-align-last="justify">
                        <fo:basic-link text-decoration="underline">
                            <xsl:call-template name="tocLinks"/>
                            <xsl:choose>
                                <xsl:when test="ead:head">
                                    <xsl:value-of select="ead:head"/>
                                </xsl:when>
                                <xsl:otherwise>Collection Inventory</xsl:otherwise>
                            </xsl:choose>
                        </fo:basic-link>
                        <xsl:text/>
                        <fo:leader leader-pattern="dots"/>
                        <xsl:text/>
                        <xsl:call-template name="tocPage"/>
                    </fo:block>   
                </xsl:if>
                
                <!--Creates a submenu for collections, record groups and series and fonds-->
                <xsl:for-each
                    select="child::*[@level = 'collection'] 
                        | child::*[@level = 'recordgrp']  | child::*[@level = 'series'] | child::*[@level = 'fonds']">
                    <fo:block text-align-last="justify" margin-left="18pt">
                        <fo:basic-link text-decoration="underline">
                            <xsl:call-template name="tocLinks"/>
                            <xsl:choose>
                                <xsl:when test="ead:head">
                                    <xsl:value-of select="child::*/ead:head"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:apply-templates select="child::*/ead:unittitle"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:basic-link>
                        <xsl:text/>
                        <fo:leader leader-pattern="dots"/>
                        <xsl:text/>
                        <xsl:call-template name="tocPage"/>
                    </fo:block>
                </xsl:for-each>
            </xsl:for-each>
        </fo:block>
    </xsl:template>

    <!-- Template generates the page numbers for the table of contents -->
    <xsl:template name="tocPage">
        <fo:page-number-citation>
            <xsl:attribute name="ref-id">
                <xsl:choose>
                    <xsl:when test="self::*/@id">
                        <xsl:value-of select="@id"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="generate-id(.)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
        </fo:page-number-citation>
    </xsl:template>
    
    <!--Orders the how ead elements appear in the PDF, order matches Table of Contents.  -->
    <xsl:template match="ead:archdesc">
        <!-- Summary Information, summary information includes citation -->
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:did"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:bioghist"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:scopecontent"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:arrangement"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:fileplan"/>

        <!-- Administrative Information  -->
        <xsl:if
            test="/ead:ead/ead:archdesc/ead:accessrestrict or
            /ead:ead/ead:archdesc/ead:userestrict or
            /ead:ead/ead:archdesc/ead:custodhist or
            /ead:ead/ead:archdesc/ead:accruals or
            /ead:ead/ead:archdesc/ead:altformavail or
            /ead:ead/ead:archdesc/ead:acqinfo or
            /ead:ead/ead:archdesc/ead:processinfo or
            /ead:ead/ead:archdesc/ead:appraisal or
            /ead:ead/ead:archdesc/ead:originalsloc | /ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt | /ead:ead/ead:eadheader/ead:revisiondesc">
            <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000" id="adminInfo"> Administrative Information </fo:block>
            <xsl:apply-templates select="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt" mode="admin"/>
            <xsl:apply-templates select="/ead:ead/ead:eadheader/ead:revisiondesc" mode="admin"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:accessrestrict"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:userestrict"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:custodhist"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:accruals"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:acqinfo"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:processinfo"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:appraisal"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:altformavail"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:originalsloc"/>
        </xsl:if>

        <!-- Related Materials -->
        <xsl:if
            test="/ead:ead/ead:archdesc/ead:relatedmaterial or /ead:ead/ead:archdesc/ead:separatedmaterial">
            <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000" id="relMat"> Related Materials </fo:block>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:relatedmaterial"/>
            <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:separatedmaterial"/>
        </xsl:if>

        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:controlaccess"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:otherfindaid"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:phystech"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:odd"/>

        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:bibliography"/>
        <xsl:apply-templates select="/ead:ead/ead:archdesc/ead:index"/>
    </xsl:template>
    
    <!-- Summary Information, generated from ead:archdesc/ead:did -->
    <xsl:template match="ead:archdesc/ead:did">
        <fo:block break-after="page">
            <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000" id="{generate-id(.)}">
                <xsl:choose>
                    <xsl:when test="ead:head">
                        <xsl:value-of select="ead:head"/>
                    </xsl:when>
                    <xsl:otherwise> Summary Information </xsl:otherwise>
                </xsl:choose>
            </fo:block>
            <fo:table space-before="0.1in" font-size="12pt" line-height="18pt" table-layout="fixed" width="100%">
                <fo:table-column column-width="2in"/>
                <fo:table-column column-width="5in"/>
                <fo:table-body>

                    <!-- Determines the order in wich elements from the archdesc did appear, 
            to change the order of appearance for the children of did
            by changing the order of the following statements.-->

                    <xsl:apply-templates select="ead:repository"/>
                    <xsl:apply-templates select="ead:origination"/>
                    <xsl:apply-templates select="ead:unittitle"/>
                    <xsl:apply-templates select="ead:unitdate"/>
                    <xsl:apply-templates select="ead:physdesc"/>
                    <xsl:apply-templates select="ead:physloc"/>
                    <xsl:apply-templates select="ead:langmaterial"/>
                    <xsl:apply-templates select="ead:materialspec"/>
                    <xsl:apply-templates select="ead:container"/>
                    <xsl:apply-templates select="ead:abstract"/>
                    <xsl:apply-templates select="ead:note"/>
                </fo:table-body>
            </fo:table>
            <xsl:apply-templates select="../ead:prefercite"/>
        </fo:block>
    </xsl:template>
   
    <!-- Template calls and formats the children of archdesc/did -->
    <xsl:template
        match="ead:archdesc/ead:did/ead:repository | ead:archdesc/ead:did/ead:unittitle | ead:archdesc/ead:did/ead:unitid | ead:archdesc/ead:did/ead:origination 
        | ead:archdesc/ead:did/ead:unitdate | ead:archdesc/ead:did/ead:physdesc | ead:archdesc/ead:did/ead:physloc 
        | ead:archdesc/ead:did/ead:abstract | ead:archdesc/ead:did/ead:langmaterial | ead:archdesc/ead:did/ead:materialspec | ead:archdesc/ead:did/ead:container">
        <fo:table-row>
            <fo:table-cell padding-bottom="18pt">
                <fo:block font-size="12pt" font-weight="bold" color="#111">
                    <xsl:choose>
                        <xsl:when test="@label">
                            <xsl:value-of select="concat(translate( substring(@label, 1, 1 ),
                                'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ' ), 
                                substring(@label, 2, string-length(@label )))" />
                            <xsl:if test="@type"> [<xsl:value-of select="@type"/>]</xsl:if>
                            <xsl:if test="self::ead:origination">
                                <xsl:choose>
                                    <xsl:when test="ead:persname[@role != ''] and contains(ead:persname/@role,' (')">
                                        - <xsl:value-of select="substring-before(ead:persname/@role,' (')"/>
                                    </xsl:when>
                                    <xsl:when test="ead:persname[@role != '']">
                                        - <xsl:value-of select="ead:persname/@role"/>  
                                    </xsl:when>
                                    <xsl:otherwise/>
                                </xsl:choose>
                            </xsl:if>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:choose>
                                <xsl:when test="self::ead:repository">Repository</xsl:when>
                                <xsl:when test="self::ead:unittitle">Title</xsl:when>
                                <xsl:when test="self::ead:unitid">ID</xsl:when>
                                <xsl:when test="self::ead:unitdate">Date<xsl:if test="@type">
                                            [<xsl:value-of select="@type"/>]</xsl:if></xsl:when>
                                <xsl:when test="self::ead:origination">
                                    <xsl:choose>
                                        <xsl:when test="ead:persname[@role != ''] and contains(ead:persname/@role,' (')">
                                            Creator - <xsl:value-of select="substring-before(ead:persname/@role,' (')"/>
                                        </xsl:when>
                                        <xsl:when test="ead:persname[@role != '']">
                                            Creator - <xsl:value-of select="ead:persname/@role"/>  
                                        </xsl:when>
                                        <xsl:otherwise>
                                            Creator        
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:when>
                                <xsl:when test="self::ead:physdesc">Extent</xsl:when>
                                <xsl:when test="self::ead:abstract">Abstract</xsl:when>
                                <xsl:when test="self::ead:physloc">Location</xsl:when>
                                <xsl:when test="self::ead:langmaterial">Language</xsl:when>
                                <xsl:when test="self::ead:materialspec">Technical</xsl:when>
                                <xsl:when test="self::ead:container">Container</xsl:when>
                                <xsl:when test="self::ead:note">Note</xsl:when>
                            </xsl:choose>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell padding-bottom="18pt">
                <fo:block>
                    <xsl:apply-templates/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>

    <!-- Template calls and formats all other children of archdesc many of 
        these elements are repeatable within the ead:dsc section as well.-->
    <xsl:template
        match="ead:bibliography | ead:odd | ead:accruals | ead:arrangement  | ead:bioghist 
        | ead:accessrestrict | ead:userestrict  | ead:custodhist | ead:altformavail | ead:originalsloc 
        | ead:fileplan | ead:acqinfo | ead:otherfindaid | ead:phystech | ead:processinfo | ead:relatedmaterial
        | ead:scopecontent  | ead:separatedmaterial | ead:appraisal">
        <xsl:choose>
            <xsl:when test="ead:head">
            <fo:block><xsl:apply-templates/></fo:block>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="parent::ead:archdesc">
                        <xsl:choose>
                            <xsl:when test="self::ead:bibliography">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Bibliography </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:odd">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Other Descriptive Data </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:accruals">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111" >
                                    <xsl:call-template name="anchor"/>Accruals </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:arrangement">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Arrangement </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:bioghist">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-variant="small-caps" font-weight="bold" color="black"
                                    padding-after="8pt" padding-before="8pt"><xsl:call-template name="anchor"/>Biography/History </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:accessrestrict">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Restrictions on Access </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:userestrict">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Restrictions on Use </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:custodhist">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/> Custodial History </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:altformavail">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Alternative Form Available </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:originalsloc">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Original Location </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:fileplan">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-variant="small-caps" font-weight="bold" color="black"
                                    padding-after="8pt" padding-before="8pt">
                                    <xsl:call-template name="anchor"/>File Plan </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:acqinfo">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Acquisition Information </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:otherfindaid">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="8pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Other Finding Aids </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:phystech">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Physical Characteristics and Technical
                                    Requirements </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:processinfo">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Processing Information </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:relatedmaterial">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Related Material </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:scopecontent">
                                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                                    <xsl:call-template name="anchor"/>Scope and Content </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:separatedmaterial">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Separated Material </fo:block>
                            </xsl:when>
                            <xsl:when test="self::ead:appraisal">
                                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                                    margin-left="-4pt" font-weight="bold" color="#111">
                                    <xsl:call-template name="anchor"/>Appraisal </fo:block>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                            margin-left="-4pt" font-weight="bold" color="#111">
                            <xsl:choose>
                                <xsl:when test="self::ead:bibliography">Bibliography</xsl:when>
                                <xsl:when test="self::ead:odd">Other Descriptive Data</xsl:when>
                                <xsl:when test="self::ead:accruals">Accruals</xsl:when>
                                <xsl:when test="self::ead:arrangement">Arrangement</xsl:when>
                                <xsl:when test="self::ead:bioghist">Biography/History</xsl:when>
                                <xsl:when test="self::ead:accessrestrict">Restrictions on Access</xsl:when>
                                <xsl:when test="self::ead:userestrict">Restrictions on Use</xsl:when>
                                <xsl:when test="self::ead:custodhist">Custodial History</xsl:when>
                                <xsl:when test="self::ead:altformavail">Alternative Form Available</xsl:when>
                                <xsl:when test="self::ead:originalsloc">Original Location</xsl:when>
                                <xsl:when test="self::ead:fileplan">File Plan</xsl:when>
                                <xsl:when test="self::ead:acqinfo">Acquisition Information</xsl:when>
                                <xsl:when test="self::ead:otherfindaid">Other Finding Aids</xsl:when>
                                <xsl:when test="self::ead:phystech">Physical Characteristics and
                                    Technical Requirements</xsl:when>
                                <xsl:when test="self::ead:processinfo">Processing Information</xsl:when>
                                <xsl:when test="self::ead:relatedmaterial">Related Material</xsl:when>
                                <xsl:when test="self::ead:scopecontent">Scope and Content</xsl:when>
                                <xsl:when test="self::ead:separatedmaterial">Separated Material</xsl:when>
                                <xsl:when test="self::ead:appraisal">Appraisal</xsl:when>
                            </xsl:choose>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
                <fo:block><xsl:apply-templates/></fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Templates for publication information  -->
    <xsl:template match="/ead:ead/ead:eadheader/ead:filedesc/ead:publicationstmt" mode="admin">
        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
            margin-left="-4pt" font-weight="bold" color="#111">
        Publication Information</fo:block>
        <fo:block>
            <xsl:apply-templates select="ead:publisher"/>
            <xsl:if test="ead:date">&#160;<xsl:apply-templates select="ead:date"/></xsl:if>
        </fo:block>
    </xsl:template>
    <!-- Templates for revision description  -->
    <xsl:template match="/ead:ead/ead:eadheader/ead:revisiondesc" mode="admin">
        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
            margin-left="-4pt" font-weight="bold" color="#111">Revision Description</fo:block>
        <fo:block><xsl:if test="ead:change/ead:item">
            <xsl:apply-templates select="ead:change/ead:item"/></xsl:if>
            <xsl:if test="ead:change/ead:date">&#160;<xsl:apply-templates select="ead:change/ead:date"/></xsl:if>
        </fo:block>        
    </xsl:template>
    
    <!-- Formats prefered citiation -->
    <xsl:template match="ead:prefercite">
       <fo:block border="1pt solid gray" padding="16pt">
            <xsl:choose>
                <xsl:when test="ead:head"><xsl:apply-templates/></xsl:when>
                <xsl:otherwise>
                    <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                        margin-left="-4pt" font-weight="bold" color="#111" padding-after="8pt" padding-before="8pt">
                        Preferred Citation</fo:block>
                    <fo:block margin="8pt"><xsl:apply-templates/></fo:block>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </xsl:template>

    <!-- Formats controlled access terms -->
    <xsl:template match="ead:controlaccess">
        <xsl:choose>
            <xsl:when test="ead:head">
                <xsl:apply-templates select="ead:head"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="parent::ead:archdesc">
                        <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                            font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000" id="{generate-id(.)}">
                            Controlled Access Headings</fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                            margin-left="-4pt" font-weight="bold" color="#111">
                            Controlled Access Headings</fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="ead:corpname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Corporate Name(s) </fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:corpname">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:famname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Family Name(s) </fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:famname">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:function">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Function(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:function">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:genreform">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Genre(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:genreform">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:geogname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Geographic Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:geogname">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:occupation">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Occupation(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:occupation">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:persname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Personal Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:persname">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:subject">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Subject(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:subject">
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block>
                                <xsl:apply-templates/>
                            </fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
    </xsl:template>

    <!-- Formats index and child elements, groups indexentry elements by type (i.e. corpname, subject...)-->
    <xsl:template match="ead:index">
        <xsl:choose>
            <xsl:when test="ead:head"/>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="parent::ead:archdesc">
                        <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                            font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000" id="{generate-id(.)}">
                            Index</fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                            margin-left="-4pt" font-weight="bold" color="#111">
                            Index</fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="child::*[not(self::ead:indexentry)]"/>
        <xsl:if test="ead:indexentry/ead:corpname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Corporate Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:corpname">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:famname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Family Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:famname">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:function">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Function(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:function">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:genreform">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Genre(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:genreform">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:geogname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Geographic Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:geogname">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:name">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:name">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:occupation">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Occupation(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:occupation">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:persname">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Personal Name(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:persname">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:subject">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Subject(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:subject">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
        <xsl:if test="ead:indexentry/ead:title">
            <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                padding-before="8pt"> Title(s)</fo:block>
            <fo:list-block margin-bottom="8pt" margin-left="8pt">
                <xsl:for-each select="ead:indexentry/ead:title">
                    <xsl:sort/>
                    <fo:list-item>
                        <fo:list-item-label end-indent="24pt">
                            <fo:block>&#x2022;</fo:block>
                        </fo:list-item-label>
                        <fo:list-item-body start-indent="24pt">
                            <fo:block><xsl:apply-templates select="."/>
                                    &#160;<xsl:apply-templates select="following-sibling::*"/></fo:block>
                        </fo:list-item-body>
                    </fo:list-item>
                </xsl:for-each>
            </fo:list-block>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ead:indexentry">
        <fo:block font-weight="bold">
            <xsl:apply-templates select="child::*[1]"/>
        </fo:block>
        <fo:block margin-left="18pt">
            <xsl:apply-templates select="child::*[2]"/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:ptrgrp">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Digital Archival Object -->
    <xsl:template match="ead:dao">
        <xsl:choose>
            <xsl:when test="child::*">
                <xsl:apply-templates/> 
                <fo:basic-link external-destination="url('{@ns2:href}')"
                    text-decoration="underline"
                    color="blue"> [<xsl:value-of select="@ns2:href"/>]</fo:basic-link>
            </xsl:when>
            <xsl:otherwise>
                <fo:basic-link external-destination="url('{@ns2:href}')"
                    text-decoration="underline"
                    color="blue"> <xsl:value-of select="@ns2:href"/></fo:basic-link>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- Linking elements, ptr and ref. -->
    <xsl:template match="ead:ptr">
        <xsl:choose>
            <xsl:when test="@target">
                <fo:basic-link internal-destination="{@target}"
                text-decoration="underline" color="blue"><xsl:value-of select="@target"/></fo:basic-link>
                <xsl:if test="following-sibling::ead:ptr">, </xsl:if>
            </xsl:when>
            <xsl:when test="@ns2:href">
                <fo:basic-link internal-destination="{@ns2:href}"
                    text-decoration="underline" color="blue"><xsl:value-of select="@target"/></fo:basic-link>
                <xsl:if test="following-sibling::ead:ptr">, </xsl:if>
            </xsl:when>
            <xsl:otherwise><xsl:apply-templates/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:ref">
        <xsl:choose>
            <xsl:when test="@target">
                <fo:basic-link internal-destination="{@target}"
                    text-decoration="underline" color="blue"><xsl:value-of select="@target"/></fo:basic-link>
                <xsl:if test="following-sibling::ead:ref">, </xsl:if>
            </xsl:when>
            <xsl:when test="@ns2:href">
                <fo:basic-link internal-destination="{@ns2:href}"
                    text-decoration="underline" color="blue"><xsl:value-of select="@target"/></fo:basic-link>
                <xsl:if test="following-sibling::ead:ref">, </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>    
  
    <!-- FLAG: need to deal with exptr and extref example:
        <extptr linktype="simple" entityref="phyllis" title="Image of Phyllis Wheatley"
        actuate="onload" show="embed">-->
    
    <!--Creates a hidden anchor tag that allows navigation within the finding aid. 
        In this stylesheet only children of the archdesc and c0* itmes call this template. 
        It can be applied anywhere in the stylesheet as the id attribute is universal. -->
    <xsl:template match="@id">
        <xsl:attribute name="id"><xsl:value-of select="."/></xsl:attribute>
    </xsl:template>
    <xsl:template name="anchor">
        <xsl:choose>
            <xsl:when test="@id">
                <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="id"><xsl:value-of select="generate-id(.)"/></xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="tocLinks">
        <xsl:attribute name="internal-destination">
            <xsl:choose>
                <xsl:when test="self::*/@id">
                    <xsl:value-of select="@id"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="generate-id(.)"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
    
    
    <!-- Formats headings throughout the finding aid -->
    <xsl:template match="ead:head[parent::*/parent::ead:archdesc]">
        <xsl:choose>
            <xsl:when
                test="parent::ead:accessrestrict or parent::ead:userestrict or
                parent::ead:custodhist or parent::ead:accruals or
                parent::ead:altformavail or parent::ead:acqinfo or
                parent::ead:processinfo or parent::ead:appraisal or
                parent::ead:originalsloc or  
                parent::ead:relatedmaterial or parent::ead:separatedmaterial">
                <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                    margin-left="-4pt" font-weight="bold" color="#111">
                    <xsl:choose>
                        <xsl:when test="parent::*/@id">
                            <xsl:attribute name="id"><xsl:value-of select="parent::*/@id"/></xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="id"><xsl:value-of select="generate-id(parent::*)"/></xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>                    
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:when>
            <xsl:when test="parent::ead:prefercite">
                <fo:block font-size="12pt" space-before="4pt" space-after="8pt"
                    margin-left="-4pt" font-weight="bold" color="#111">
                    <xsl:choose>
                        <xsl:when test="parent::*/@id">
                            <xsl:attribute name="id"><xsl:value-of select="parent::*/@id"/></xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="id"><xsl:value-of select="generate-id(parent::*)"/></xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:apply-templates/>
                </fo:block>                
            </xsl:when>
            <xsl:otherwise>
                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="24pt" border-bottom="1pt dashed #666" border-top="2pt solid #000">
                    <xsl:choose>
                        <xsl:when test="parent::*/@id">
                            <xsl:attribute name="id"><xsl:value-of select="parent::*/@id"/></xsl:attribute>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:attribute name="id"><xsl:value-of select="generate-id(parent::*)"/></xsl:attribute>
                        </xsl:otherwise>
                    </xsl:choose>
                    <xsl:apply-templates/>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:head">
        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
            margin-left="-4pt" font-weight="bold" color="#111">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <!--Bibref, choose statement decides if the citation is inline, if there is a parent element
        or if it is its own line, typically when it is a child of the bibliography element.-->
    <xsl:template match="ead:bibref">
        <xsl:choose>
            <xsl:when test="parent::ead:p">
                <xsl:choose>
                    <xsl:when test="@ns2:href">
                        <fo:basic-link external-destination="url('{@ns2:href}')"><xsl:apply-templates/></fo:basic-link>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <fo:block  margin-bottom="8pt">
                    <xsl:choose>
                        <xsl:when test="@ns2:href">
                            <fo:basic-link external-destination="url('{@ns2:href}')"><xsl:apply-templates/></fo:basic-link>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:apply-templates/>
                        </xsl:otherwise>
                    </xsl:choose>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Puts a space between sibling elements -->
    <xsl:template match="child::*">
        <xsl:if test="preceding-sibling::*">&#160;</xsl:if>
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="ead:p">
        <fo:block margin-bottom="8pt">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <!--Formats a simple table. The width of each column is defined by the colwidth attribute in a colspec element.-->
    <xsl:template match="ead:table">
        <xsl:for-each select="tgroup">
            <fo:table table-layout="fixed" width="100%" space-after="24pt" 
                space-before="36pt" font-size="12pt" line-height="18pt" 
                border-top="1pt solid #000" border-bottom="1pt solid #000">
                <xsl:for-each select="ead:colspec">
                    <fo:table-column column-width="{@colwidth}"/>
                </xsl:for-each>
                <fo:table-body>
                    <xsl:for-each select="ead:thead">
                        <xsl:for-each select="ead:row">
                            <fo:table-row>
                                <xsl:for-each select="ead:entry">
                                    <fo:table-cell border="1pt solid #fff" background-color="#000" padding="8pt">
                                        <fo:block font-size="14pt" font-weight="bold" color="#111">
                                            <xsl:value-of select="."/>
                                        </fo:block>
                                    </fo:table-cell>
                                </xsl:for-each>
                            </fo:table-row>
                        </xsl:for-each>
                    </xsl:for-each>
                    <xsl:for-each select="ead:tbody">
                        <xsl:for-each select="ead:row">
                            <fo:table-row>
                                <xsl:for-each select="ead:entry">
                                    <fo:table-cell padding="8pt">
                                        <fo:block>
                                            <xsl:value-of select="."/>
                                        </fo:block>
                                    </fo:table-cell>
                                </xsl:for-each>
                            </fo:table-row>
                        </xsl:for-each>
                    </xsl:for-each>
                </fo:table-body>
            </fo:table>
        </xsl:for-each>
    </xsl:template>
    <!-- Formats unitdates and dates -->
    <xsl:template match="ead:unitdate">
        <xsl:if test="preceding-sibling::*">&#160;</xsl:if>
        <xsl:choose>
            <xsl:when test="@type = 'bulk'">
                (<xsl:apply-templates/>)                            
            </xsl:when>
            <xsl:otherwise><xsl:apply-templates/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:date">
        <xsl:apply-templates/>
    </xsl:template>
    <!-- Formats unitTitle -->
    <xsl:template match="ead:unittitle">
        <xsl:choose>
            <xsl:when test="child::ead:unitdate[@type='bulk']">
                <xsl:apply-templates select="node()[not(self::ead:unitdate[@type='bulk'])]"/>
                <xsl:apply-templates select="ead:date[@type='bulk']"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- Following five templates output chronlist and children in a table -->
    <xsl:template match="ead:chronlist">
        <fo:table table-layout="fixed" width="100%" space-before="36pt" font-size="12pt" line-height="18pt" border-top="1pt solid #000" border-bottom="1pt solid #000" space-after="24pt">
            <fo:table-body>
                <xsl:apply-templates/>
            </fo:table-body>    
        </fo:table>
    </xsl:template>
    <xsl:template match="ead:chronlist/ead:listhead">
        <fo:table-row>
            <fo:table-cell border="1pt solid #fff" background-color="#000" padding="8pt">
                <fo:block font-size="14pt" font-weight="bold" color="#fff">
                    <xsl:apply-templates select="ead:head01"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1pt solid #fff" background-color="#000" padding="8pt">
                <fo:block font-size="14pt" font-weight="bold" color="#fff">
                    <xsl:apply-templates select="ead:head02"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <xsl:template match="ead:chronlist/ead:head">
        <fo:table-row>
            <fo:table-cell border="1pt solid #fff" background-color="#000" number-columns-spanned="2" padding="8pt">
                <fo:block font-size="14pt" font-weight="bold" color="#fff">
                    <xsl:apply-templates/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <xsl:template match="ead:chronitem">
        <fo:table-row>
            <xsl:attribute name="background-color">
                <xsl:choose>
                    <xsl:when test="(position() mod 2 = 0)">#eee</xsl:when>
                    <xsl:otherwise>#fff</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <fo:table-cell>
                <fo:block>
                    <xsl:apply-templates select="ead:date"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell>
                <fo:block>
                    <xsl:apply-templates select="descendant::ead:event"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
    <xsl:template match="ead:event">
        <xsl:choose>
            <xsl:when test="following-sibling::*">
                <fo:block><xsl:apply-templates/></fo:block>
                <fo:block/>
            </xsl:when>
            <xsl:otherwise>
                <fo:block><xsl:apply-templates/></fo:block>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <!-- Output for a variety of list types -->
    <xsl:template match="ead:list">
        <xsl:if test="ead:head">
            <fo:block font-size="12pt" space-before="18pt" space-after="4pt"
                font-weight="bold" color="#111">
                <xsl:value-of select="ead:head"/>
            </fo:block>
        </xsl:if>
        <fo:list-block margin-bottom="8pt" margin-left="8pt">
            <xsl:apply-templates/>
        </fo:list-block>
    </xsl:template>
    <xsl:template match="ead:list/ead:head"/>
    <xsl:template match="ead:list/ead:item">
        <xsl:choose>
            <xsl:when test="parent::*/@type = 'ordered'">
                <xsl:choose>
                    <xsl:when test="parent::*/@numeration = 'arabic'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="1"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'upperalpha'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="A"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'loweralpha'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="a"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'upperroman'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="I"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'lowerroman'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="i"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="1"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:when test="parent::*/@type='simple'">
                <fo:list-item>
                    <fo:list-item-label end-indent="24pt">
                        <fo:block>&#x2022;</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="24pt">
                        <fo:block>
                            <xsl:apply-templates/>
                        </fo:block>
                    </fo:list-item-body>
                </fo:list-item>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="parent::*/@numeration = 'arabic'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="1"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'upperalpha'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="A"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'loweralpha'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="a"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'upperroman'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="I"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:when test="parent::*/@numeration = 'lowerroman'">
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>
                                    <xsl:number format="i"/>
                                </fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:list-item>
                            <fo:list-item-label end-indent="24pt">
                                <fo:block>&#x2022;</fo:block>
                            </fo:list-item-label>
                            <fo:list-item-body start-indent="24pt">
                                <fo:block>
                                    <xsl:apply-templates/>
                                </fo:block>
                            </fo:list-item-body>
                        </fo:list-item>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="ead:defitem">
        <fo:list-item>
            <fo:list-item-label>
                <fo:block/>
            </fo:list-item-label>
            <fo:list-item-body>
                <fo:block font-weight="bold">
                    <xsl:apply-templates select="ead:label"/>
                </fo:block>
                <fo:block margin-left="18pt">
                    <xsl:apply-templates select="ead:item"/>
                </fo:block>
            </fo:list-item-body>
        </fo:list-item>
    </xsl:template>

    <!-- Formats list as tabel if list has listhead element  -->
    <xsl:template match="ead:list[child::ead:listhead]">
        <fo:table table-layout="fixed" space-before="24pt" space-after="24pt" font-size="12pt" line-height="18pt" width="4.5in" margin-left="8pt" border-top="1pt solid #000" border-bottom="1pt solid #000">
            <fo:table-body>
            <fo:table-row>
                <fo:table-cell border="1pt solid #fff" background-color="#000" padding="8pt">
                    <fo:block font-size="14pt" font-weight="bold" color="#fff">
                        <xsl:apply-templates select="ead:listhead/ead:head01"/>
                    </fo:block>
                </fo:table-cell>
                <fo:table-cell border="1pt solid #fff" background-color="#000" padding="8pt">
                    <fo:block font-size="14pt" font-weight="bold" color="#fff">
                        <xsl:apply-templates select="ead:listhead/ead:head02"/>
                    </fo:block>
                </fo:table-cell>
            </fo:table-row>
            <xsl:for-each select="ead:defitem">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block margin-left="8pt">
                            <xsl:apply-templates select="ead:label"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block margin-left="8pt">
                            <xsl:apply-templates select="ead:item"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </xsl:for-each>
            </fo:table-body>     
        </fo:table>
    </xsl:template>

    <!-- Formats notestmt and notes -->
    <xsl:template match="ead:notestmt">
        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
            margin-left="-4pt" font-weight="bold" color="#111" id="{generate-id(.)}"> Note</fo:block>
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="ead:note">
        <xsl:choose>
            <xsl:when test="parent::ead:notestmt">
                <fo:block><xsl:apply-templates/></fo:block>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="@label">
                        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                            margin-left="-4pt" font-weight="bold" color="#111"  id="{generate-id(.)}">
                            <xsl:value-of select="@label"/>
                        </fo:block>
                        <fo:block><xsl:apply-templates/></fo:block>
                    </xsl:when>
                    <xsl:otherwise>
                        <fo:block font-size="12pt" space-before="18pt" space-after="8pt"
                            margin-left="-4pt" font-weight="bold" color="#111"  id="{generate-id(.)}"> Note </fo:block>    
                        <fo:block><xsl:apply-templates/></fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- Generic text display elements -->
    <xsl:template match="ead:lb">
        <fo:block/>
    </xsl:template>
    <xsl:template match="ead:blockquote">
        <fo:block margin="18pt">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>
    <xsl:template match="ead:emph">
        <fo:inline font-style="italic">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <!--Render elements -->
    <xsl:template match="*[@render = 'bold'] | *[@altrender = 'bold'] ">
        <fo:inline font-weight="bold">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'bolddoublequote'] | *[@altrender = 'bolddoublequote']">
        <fo:inline font-weight="bold"><xsl:if test="preceding-sibling::*"> &#160;</xsl:if>"<xsl:apply-templates/>"</fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'boldsinglequote'] | *[@altrender = 'boldsinglequote']">
        <fo:inline font-weight="bold"><xsl:if test="preceding-sibling::*"> &#160;</xsl:if>'<xsl:apply-templates/>'</fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'bolditalic'] | *[@altrender = 'bolditalic']">
        <fo:inline font-weight="bold" font-style="italic">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'boldsmcaps'] | *[@altrender = 'boldsmcaps']">
        <fo:inline font-weight="bold" font-variant="small-caps">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'boldunderline'] | *[@altrender = 'boldunderline']">
        <fo:inline font-weight="bold" border-bottom="1pt solid #000">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'doublequote'] | *[@altrender = 'doublequote']">
        <xsl:if test="preceding-sibling::*"> &#160;</xsl:if>"<xsl:apply-templates/>" </xsl:template>
    <xsl:template match="*[@render = 'italic'] | *[@altrender = 'italic']">
        <fo:inline font-style="italic">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'singlequote'] | *[@altrender = 'singlequote']">
        <xsl:if test="preceding-sibling::*"> &#160;</xsl:if>'<xsl:apply-templates/>' </xsl:template>
    <xsl:template match="*[@render = 'smcaps'] | *[@altrender = 'smcaps']">
        <fo:inline font-variant="small-caps">
            <xsl:if test="preceding-sibling::*"> &#160;</xsl:if><xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'sub'] | *[@altrender = 'sub']">
        <fo:inline baseline-shift="sub">
           <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'super'] | *[@altrender = 'super']">
        <fo:inline baseline-shift="super">
           <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    <xsl:template match="*[@render = 'underline'] | *[@altrender = 'underline']">
        <fo:inline border-bottom="1pt solid #000">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <!-- *** Begin templates for Container List *** -->
    <xsl:template match="ead:archdesc/ead:dsc">
        <xsl:choose>
            <xsl:when test="ead:head">
                <xsl:apply-templates select="ead:head"/>
            </xsl:when>
            <xsl:otherwise>
                <fo:block font-size="16pt" space-before="36pt" space-after="18pt"
                    font-weight="bold" color="black" margin-left="-8pt" padding-after="8pt" padding-before="8pt" id="{generate-id(.)}">
                    Collection Inventory
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
        <fo:table table-layout="fixed" space-after="24pt" space-before="36pt" width="7.25in" font-size="12pt" line-height="18pt" border-top="1pt solid #000" border-bottom="1pt solid #000">
            <fo:table-column column-number="1" column-width="3.25in"/>
            <fo:table-column column-number="2" column-width="1in"/>
            <fo:table-column column-number="3" column-width="1in"/>
            <fo:table-column column-number="4" column-width="1in"/>
            <fo:table-column column-number="5" column-width="1in"/>
            <fo:table-body>
               <xsl:apply-templates select="*[not(self::ead:head)]"/>
            </fo:table-body>
        </fo:table>
        
    </xsl:template>
    
    <!--This section of the stylesheet creates a div for each c01 or c 
        It then recursively processes each child component of the c01 by 
        calling the clevel template. -->
    <xsl:template match="ead:c">
                <xsl:call-template name="clevel"/>
                <xsl:for-each select="ead:c">
                    <xsl:call-template name="clevel"/> 
                    <xsl:for-each select="ead:c">
                        <xsl:call-template name="clevel"/>    
                        <xsl:for-each select="ead:c">
                            <xsl:call-template name="clevel"/>
                            <xsl:for-each select="ead:c">
                                <xsl:call-template name="clevel"/>
                                <xsl:for-each select="ead:c">
                                    <xsl:call-template name="clevel"/> 
                                    <xsl:for-each select="ead:c">
                                        <xsl:call-template name="clevel"/>
                                        <xsl:for-each select="ead:c">
                                            <xsl:call-template name="clevel"/>
                                            <xsl:for-each select="ead:c">
                                                <xsl:call-template name="clevel"/>    
                                            </xsl:for-each>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                    </xsl:for-each>
                </xsl:for-each> 
    </xsl:template>
    <xsl:template match="ead:c01">
        <xsl:call-template name="clevel"/>
        <xsl:for-each select="ead:c02">
            <xsl:call-template name="clevel"/>
            <xsl:for-each select="ead:c03">
                <xsl:call-template name="clevel"/>
                <xsl:for-each select="ead:c04">
                    <xsl:call-template name="clevel"/>
                    <xsl:for-each select="ead:c05">
                        <xsl:call-template name="clevel"/>
                        <xsl:for-each select="ead:c06">
                            <xsl:call-template name="clevel"/>
                            <xsl:for-each select="ead:c07">
                                <xsl:call-template name="clevel"/>
                                <xsl:for-each select="ead:c08">
                                    <xsl:call-template name="clevel"/>
                                    <xsl:for-each select="ead:c09">
                                        <xsl:call-template name="clevel"/>
                                        <xsl:for-each select="ead:c10">
                                            <xsl:call-template name="clevel"/>
                                            <xsl:for-each select="ead:c11">
                                                <xsl:call-template name="clevel"/>
                                                <xsl:for-each select="ead:c12">
                                                    <xsl:call-template name="clevel"/>
                                                </xsl:for-each>
                                            </xsl:for-each>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:for-each>
                            </xsl:for-each>
                        </xsl:for-each>
                    </xsl:for-each>
                </xsl:for-each>
        </xsl:for-each>
            </xsl:for-each>
    </xsl:template>
    <!--This is a named template that processes all c0* elements  -->
    <xsl:template name="clevel">
        <!-- Establishes which level is being processed in order to provided indented displays. 
        Indents handdled by CSS margins-->
        <!-- Establishes which level is being processed in order to provided indented displays. 
            Indents handled by CSS margins-->
        <xsl:param name="level" />
        <xsl:variable name="clevelMargin">
            <xsl:choose>
                <xsl:when test="$level = 01">0in</xsl:when>
                <xsl:when test="$level = 02">.2in</xsl:when>
                <xsl:when test="$level = 03">.4in</xsl:when>
                <xsl:when test="$level = 04">.6in</xsl:when>
                <xsl:when test="$level = 05">.8in</xsl:when>
                <xsl:when test="$level = 06">1in</xsl:when>
                <xsl:when test="$level = 07">1.2in</xsl:when>
                <xsl:when test="$level = 08">1.4in</xsl:when>
                <xsl:when test="$level = 09">1.6in</xsl:when>
                <xsl:when test="$level = 10">1.8in</xsl:when>
                <xsl:when test="$level = 11">1.9in</xsl:when>
                <xsl:when test="$level = 12">2in</xsl:when>
                <xsl:when test="../ead:c01">0in</xsl:when>
                <xsl:when test="../ead:c02">.2in</xsl:when>
                <xsl:when test="../ead:c03">.4in</xsl:when>
                <xsl:when test="../ead:c04">.6in</xsl:when>
                <xsl:when test="../ead:c05">.8in</xsl:when>
                <xsl:when test="../ead:c06">1in</xsl:when>
                <xsl:when test="../ead:c07">1.2in</xsl:when>
                <xsl:when test="../ead:c08">1.4in</xsl:when>
                <xsl:when test="../ead:c09">1.6in</xsl:when>
                <xsl:when test="../ead:c10">1.8in</xsl:when>
                <xsl:when test="../ead:c11">1.9in</xsl:when>
                <xsl:when test="../ead:c12">2.0in</xsl:when>
                <xsl:otherwise>0in</xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <!-- Establishes a class for even and odd rows in the table for color coding. 
            Colors are Declared in the CSS. -->
        <xsl:variable name="colorClass">
            <xsl:choose>
                <xsl:when test="ancestor-or-self::*[@level='file' or @level='item'  or @level='otherlevel']">
                    <xsl:choose>
                        <xsl:when test="(position() mod 2 = 0)">#fff</xsl:when>
                        <xsl:otherwise>#eee</xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <!-- Processes the all child elements of the c or c0* level -->
        <xsl:for-each select=".">
            <xsl:choose>
                <!--Formats Series and Groups  -->
                <xsl:when test="@level='subcollection' or @level='subgrp' or @level='series' 
                    or @level='subseries'or @level='collection'or @level='fonds' or 
                    @level='recordgrp' or @level='subfonds' or @level='class' or (@level='otherlevel' and not(child::ead:did/ead:container))">                    
                    <fo:table-row background-color="{$colorClass}" border-bottom="1pt solid #fff">
                        <xsl:choose>
                            <xsl:when test="@level='subcollection' or @level='subgrp' or @level='subseries' or @level='subfonds'">
                                <xsl:attribute name="background-color">#dddddd</xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise><xsl:attribute name="background-color">#bbbbbb</xsl:attribute></xsl:otherwise>
                        </xsl:choose>
                        <xsl:choose>
                            <xsl:when test="ead:did/ead:container">
                                <fo:table-cell>
                                    <xsl:choose>                                
                                        <xsl:when test="count(ead:did/ead:container) &lt; 1">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>5</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(ead:did/ead:container) = 1">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>4</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(ead:did/ead:container) = 2">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>3</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(ead:did/ead:container) = 3">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>2</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:otherwise/>
                                    </xsl:choose>    
                                    <fo:block padding-left="{$clevelMargin}" margin-left="8pt">
                                        <xsl:call-template name="anchor"/>
                                        <xsl:apply-templates select="ead:did" mode="dsc"/>
                                        <xsl:apply-templates select="child::*[not(ead:did) and not(self::ead:did)]"/>
                                    </fo:block>
                                </fo:table-cell>
                                <xsl:for-each select="ead:did/ead:container">    
                                    <fo:table-cell>    
                                        <fo:block text-align="center" font-weight="bold">
                                            <xsl:value-of select="@type"/></fo:block>
                                        <fo:block text-align="center">
                                            <xsl:value-of select="."/>
                                        </fo:block>        
                                    </fo:table-cell>
                                </xsl:for-each>
                            </xsl:when>
                            <xsl:otherwise>
                                <fo:table-cell number-columns-spanned="5" padding="8pt">
                                    <fo:block padding-left="{$clevelMargin}">
                                        <xsl:call-template name="anchor"/>
                                        <xsl:apply-templates select="ead:did" mode="dsc"/>
                                        <xsl:apply-templates select="child::*[not(ead:did) and not(self::ead:did)]"/>
                                    </fo:block>
                                </fo:table-cell>    
                            </xsl:otherwise>
                        </xsl:choose>
                       </fo:table-row>
                   </xsl:when>         
              
                <!--Items/Files with multiple formats linked using parent and id attributes -->
                <!-- EDITIED 5/31/12: Fixes bug with multiple instances -->
                <xsl:when test="count(child::*/ead:container/@id) &gt; 1">
                    <fo:table-row background-color="{$colorClass}" border-bottom="1pt solid #fff">
                        <fo:table-cell number-columns-spanned="5" padding="8pt">
                            <fo:block padding-left="{$clevelMargin}">
                            <xsl:apply-templates select="ead:did" mode="dsc"/>  
                            <fo:table table-layout="fixed" space-after="12pt" 
                                space-before="12pt" width="6in" font-size="12pt" 
                                line-height="18pt" margin-left="18pt" padding="0in"
                                border-top="1pt solid #666" border-bottom="1pt solid #666">
                                <fo:table-column column-number="1" column-width="1.9in"/>
                                <fo:table-column column-number="2" column-width=".1in"/>
                                <fo:table-column column-number="3" column-width="1in"/>
                                <fo:table-column column-number="4" column-width="1in"/>
                                <fo:table-column column-number="5" column-width="1in"/>
                                <fo:table-column column-number="6" column-width="1in"/>
                                <fo:table-body>

                                <xsl:for-each select="child::*/ead:container[@label]">
                                    <xsl:variable name="id" select="@id"/>
                                    <xsl:variable name="container" select="count(../ead:container[@parent = $id] | ../ead:container[@id = $id])"/>
                                    <fo:table-row>
                                        <fo:table-cell>
                                            <fo:block>&#160;</fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <xsl:choose>
                                                <xsl:when test="$container = ''"><xsl:attribute name="number-columns-spanned">4</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 1"><xsl:attribute name="number-columns-spanned">4</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 2"><xsl:attribute name="number-columns-spanned">3</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 3"><xsl:attribute name="number-columns-spanned">2</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 4"/>
                                                <xsl:otherwise/>
                                            </xsl:choose>
                                            <fo:block>&#160;</fo:block>
                                        </fo:table-cell>
                                        <xsl:for-each select="../ead:container[@parent = $id] | ../ead:container[@id = $id]">
                                            <fo:table-cell>
                                                <fo:block font-weight="bold">
                                                    <xsl:value-of select="@type"/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </xsl:for-each>
                                    </fo:table-row>
                                    
                                    <fo:table-row>
                                        <fo:table-cell>
                                            <fo:block padding-before="8pt" margin-left="16pt">
                                                <xsl:value-of select="@label"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <xsl:choose>
                                                <xsl:when test="$container = ''"><xsl:attribute name="number-columns-spanned">4</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 1"><xsl:attribute name="number-columns-spanned">4</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 2"><xsl:attribute name="number-columns-spanned">3</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 3"><xsl:attribute name="number-columns-spanned">2</xsl:attribute></xsl:when>
                                                <xsl:when test="$container = 4"/>
                                                <xsl:otherwise/>
                                            </xsl:choose>
                                            <fo:block>&#160;</fo:block>
                                        </fo:table-cell>
                                        <xsl:for-each select="../ead:container[@parent = $id] | ../ead:container[@id = $id]">
                                            <fo:table-cell>
                                                <fo:block>
                                                    <xsl:value-of select="."/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </xsl:for-each>
                                    </fo:table-row>
                                </xsl:for-each>
                                    <xsl:if test="descendant-or-self::ead:dao">
                                        <xsl:for-each select="descendant-or-self::ead:dao">
                                            <fo:table-row>
                                                <fo:table-cell>
                                                    <fo:block padding-before="8pt" margin-left="16pt">        
                                                        Digital Object
                                                    </fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell>
                                                    <fo:block padding-before="8pt">&#160;</fo:block>
                                                </fo:table-cell>
                                                <fo:table-cell number-columns-spanned="4">
                                                    <fo:block padding-before="8pt">
                                                        <xsl:apply-templates select="."/>
                                                    </fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                        </xsl:for-each>
                                    </xsl:if>
                                </fo:table-body>
                            </fo:table>
                            <xsl:apply-templates select="*[not(self::ead:did) and not(descendant-or-self::ead:dao) and
                                    not(self::ead:c) and not(self::ead:c02) and not(self::ead:c03) and
                                    not(self::ead:c04) and not(self::ead:c05) and not(self::ead:c06) and not(self::ead:c07)
                                    and not(self::ead:c08) and not(self::ead:c09) and not(self::ead:c10) and not(self::ead:c11) and not(self::ead:c12)]"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:when>
                
                <!-- Items/Files--> 
                <xsl:when test="@level='file' or @level='item' or (@level='otherlevel'and child::ead:did/ead:container)">
                    <!-- Variables to  for Conainer headings, used only if headings are different from preceding heading -->
                    <xsl:variable name="container" select="string(ead:did/ead:container[1]/@type)"/>
                    <xsl:variable name="container2" select="string(ead:did/ead:container[2]/@type)"/>
                    <xsl:variable name="container3" select="string(ead:did/ead:container[3]/@type)"/>
                    <xsl:variable name="container4" select="string(ead:did/ead:container[4]/@type)"/>
                    <!-- Counts contianers for current and preceding instances and if different inserts a heading -->
                    <xsl:variable name="containerCount" select="count(ead:did/ead:container)"/>
                    <xsl:variable name="sibContainerCount" select="count(preceding-sibling::*[1]/ead:did/ead:container)"/>
                    <!-- Variable estabilishes previouse container types for comparisson to current container. -->
                    <xsl:variable name="sibContainer" select="string(preceding-sibling::*[1]/ead:did/ead:container[1]/@type)"/>
                    <xsl:variable name="sibContainer2" select="string(preceding-sibling::*[1]/ead:did/ead:container[2]/@type)"/>
                    <xsl:variable name="sibContainer3" select="string(preceding-sibling::*[1]/ead:did/ead:container[3]/@type)"/>
                    <xsl:variable name="sibContainer4" select="string(preceding-sibling::*[1]/ead:did/ead:container[4]/@type)"/>
                    <xsl:if test="$container != $sibContainer or $container2 != $sibContainer2 or $container3 != $sibContainer3 or $container4 != $sibContainer4 or$containerCount != $sibContainerCount">
                        <xsl:if test="ead:did/ead:container">
                            <fo:table-row border-bottom="1pt solid #fff">
                                <fo:table-cell>
                                    <xsl:choose>                                
                                        <xsl:when test="count(descendant::ead:did[ead:container][1]/ead:container) &lt; 1">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>5</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(descendant::ead:did[ead:container][1]/ead:container) = 1">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>4</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(descendant::ead:did[ead:container][1]/ead:container) = 2">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>3</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:when test="count(descendant::ead:did[ead:container][1]/ead:container) = 3">
                                            <xsl:attribute name="number-columns-spanned">
                                                <xsl:text>2</xsl:text>
                                            </xsl:attribute>
                                        </xsl:when>
                                        <xsl:otherwise/>
                                    </xsl:choose>    
                                    <fo:block/>
                                </fo:table-cell>
                                <xsl:for-each select="ead:did/ead:container">    
                                    <fo:table-cell>    
                                        <fo:block text-align="center" font-weight="bold"><xsl:value-of select="@type"/></fo:block>        
                                    </fo:table-cell>
                                </xsl:for-each>
                            </fo:table-row>
                        </xsl:if> 
                    </xsl:if>
                    
                    <fo:table-row background-color="{$colorClass}" border-bottom="1pt solid #fff">
                        <fo:table-cell padding="8pt">
                            <xsl:choose>
                                <xsl:when test="count(ead:did/ead:container) &lt; 1">
                                    <xsl:attribute name="number-columns-spanned">
                                        <xsl:text>5</xsl:text>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:when test="count(ead:did/ead:container) =1">
                                    <xsl:attribute name="number-columns-spanned">
                                        <xsl:text>4</xsl:text>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:when test="count(ead:did/ead:container) = 2">
                                    <xsl:attribute name="number-columns-spanned">
                                        <xsl:text>3</xsl:text>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:when test="count(ead:did/ead:container) = 3">
                                    <xsl:attribute name="number-columns-spanned">
                                        <xsl:text>2</xsl:text>
                                    </xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise/>
                            </xsl:choose>   
                            <fo:block padding-left="{$clevelMargin}">
                                <xsl:apply-templates select="ead:did" mode="dsc"/>  
                                <xsl:apply-templates select="*[not(self::ead:did) and 
                                    not(self::ead:c) and not(self::ead:c02) and not(self::ead:c03) and
                                    not(self::ead:c04) and not(self::ead:c05) and not(self::ead:c06) and not(self::ead:c07)
                                    and not(self::ead:c08) and not(self::ead:c09) and not(self::ead:c10) and not(self::ead:c11) and not(self::ead:c12)]"/>
                            </fo:block>
                        </fo:table-cell>
                    <!-- Containers -->    
                        <xsl:for-each select="ead:did/ead:container">    
                            <fo:table-cell padding="8pt">
                                <fo:block padding-left="{$clevelMargin}" text-align="center">
                                    <xsl:value-of select="."/>
                                </fo:block>
                            </fo:table-cell> 
                        </xsl:for-each>
                    </fo:table-row>
                
                </xsl:when>
                <xsl:otherwise>
                    <fo:table-row background-color="{$colorClass}" border-bottom="1pt solid #fff">
                        <fo:table-cell padding="8pt">
                            <fo:block padding-left="{$clevelMargin}" padding-before="8pt">
                            <xsl:apply-templates select="ead:did" mode="dsc"/>
                                <xsl:apply-templates select="*[not(self::ead:did) and 
                                    not(self::ead:c) and not(self::ead:c02) and not(self::ead:c03) and
                                    not(self::ead:c04) and not(self::ead:c05) and not(self::ead:c06) and not(self::ead:c07)
                                    and not(self::ead:c08) and not(self::ead:c09) and not(self::ead:c10) and not(self::ead:c11) and not(self::ead:c12)]"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="ead:did" mode="dsc">
        <xsl:choose>
            <xsl:when test="../@level='subcollection' or ../@level='subgrp' or ../@level='series' 
                or ../@level='subseries'or ../@level='collection'or ../@level='fonds' or 
                ../@level='recordgrp' or ../@level='subfonds'">   
                <fo:block font-size="12pt" space-before="18pt" space-after="18pt"
                    font-variant="small-caps" font-weight="bold" color="#111" padding-after="8pt"
                    padding-before="8pt" id="{generate-id(.)}">
                    <fo:marker marker-class-name="series-title">
                        <fo:block font-size="10pt" color="gray" space-after="0.05in" space-before="0.05in" font-weight="bold" border-bottom="solid" border-bottom-color="gray" padding-before=".05in" padding-after=".05in">
                            <xsl:value-of select="substring(normalize-space(ead:unittitle),0,75)"/><xsl:if test="string-length(normalize-space(ead:unittitle)) &gt; 75">...</xsl:if>
                        </fo:block>
                    </fo:marker>
                    <xsl:call-template name="component-did-core"/>
                </fo:block>
            </xsl:when>
            <!--Otherwise render the text in its normal font.-->
            <xsl:otherwise>
                <fo:block padding-before="8pt"><xsl:call-template name="component-did-core"/></fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="component-did-core">
        <!--Inserts unitid and a space if it exists in the markup.-->
        <xsl:if test="ead:unitid">
            <xsl:apply-templates select="ead:unitid"/>
            <xsl:text>&#160;</xsl:text>
        </xsl:if>
        <!--Inserts origination and a space if it exists in the markup.-->
        <xsl:if test="ead:origination">
            <xsl:apply-templates select="ead:origination"/>
            <xsl:text>&#160;</xsl:text>
        </xsl:if>
        <!--This choose statement selects between cases where unitdate is a child of unittitle and where it is a separate child of did.-->
        <xsl:choose>
            <!--This code processes the elements when unitdate is a child of unittitle.-->
            <xsl:when test="ead:unittitle/ead:unitdate">
                <xsl:apply-templates select="ead:unittitle"/>
            </xsl:when>
            <!--This code process the elements when unitdate is not a child of untititle-->
            <xsl:otherwise>
                <xsl:apply-templates select="ead:unittitle"/>
                <xsl:text>&#160;</xsl:text>
                <xsl:for-each select="ead:unitdate[not(self::ead:unitdate[@type='bulk'])]">
                    <xsl:apply-templates/>
                    <xsl:text>&#160;</xsl:text>
                </xsl:for-each>
                <xsl:for-each select="ead:unitdate[@type = 'bulk']">
                    (<xsl:apply-templates/>)
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="ead:physdesc">
            <xsl:text>&#160;</xsl:text>
            <xsl:apply-templates select="ead:physdesc"/>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>