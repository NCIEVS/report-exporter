<template>
	<div class="subfooter">
		<div class="subfooter navbar-inverse navbar-fixed-bottom">
			<div class="center-block">
				<div class=" evsTable small navbar-text text-left">
            <tbody>
              <tr valign="top">
                <td class="evsData">
                  <a  alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
                    <img  alt="EVS" src="../assets/EVSTile.png" width="77" height="38px" border="0"></a>
                </td>
                <td class="evsData" width="3px;">
                </td>
                <td class="evsData textbody" valign="top">
                  <a  alt="NCIm" href="https://ncim.nci.nih.gov/ncimbrowser" target="_blank">NCI Enterprise Vocabulary Services</a>
                  : Terminology resources and services for NCI and the biomedical community.
                </td>
                <td class="evsData">
                  <a alt="NCIm" href="https://ncim.nci.nih.gov/ncimbrowser" target="_blank">
                    <img  alt="NCIm" src="../assets/NCImTile.png" width="77" height="38px" border="0"></a>
                </td>
                <td class="evsData" width="3px;">
                </td>
                <td class="evsData textbody" valign="top">
                  <a alt="NCIm" href="https://ncim.nci.nih.gov/ncimbrowser" target="_blank">NCI Metathesaurus</a>
                  : Comprehensive database of 6,700,000 terms from over 85 terminologies.
                </td>
              </tr>
              <tr valign="top">
                <td>
                  <a alt="NCI Term Browser" href="/ncitbrowser/start.jsf">
                    <img alt="NCI Term Browser" src="../assets/EVSTermsBrowserTile.png" width="77" height="38px" border="0">
                  </a>
                </td>
                <td width="3px;">
                </td>
                <td class="textbody" valign="top">
                  <a alt="NCI Term Browser" href="/ncitbrowser/start.jsf">NCI Term Browser</a>
                  : NCI and other terminologies in an integrated environment.
                </td>
                <td>
                  <a alt="NCI Terminology Resources" href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank">
                    <img alt="NCI Terminology Resources" src="../assets/Cancer_govTile.png" border="0"></a>
                </td>
                <td width="3px;"></td>
                <td class="textbody" valign="top">
                  <a alt="NCI Terminology Resources" href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank">NCI Terminology Resources</a>
                  : More information on NCI dictionaries and resources.
                </td>
              </tr>
            </tbody>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
  name: 'SubFooterBar',
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.subfooter {
	/* position: relative; */
	bottom: 0;
	width: 100%;
  color: rgba(255,255,255, 1);
  background-color: #007dbd;
  padding: 0 30px 2px;
	text-align: center;
}
.evsTable {
  vertical-align: text-bottom;
  padding-bottom: 0px;
}
.evsData {
  padding-bottom: 5px;
}
p {
	margin: 0 auto;
	height: 15px;
	padding-top: 0px;
	padding-bottom: 0px;
}
a {
	font-size: 12px;
	color: #fff;
}
a:link {
	color: #fff;
	text-decoration: none
}
a:hover {
	color: lightgray;
}
</style>
