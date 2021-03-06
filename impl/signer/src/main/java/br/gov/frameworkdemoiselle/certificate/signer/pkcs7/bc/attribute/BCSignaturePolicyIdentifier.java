/*
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 * ----------------------------------------------------------------------------
 * This file is part of Demoiselle Framework.
 *
 * Demoiselle Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 * ----------------------------------------------------------------------------
 * Este arquivo é parte do Framework Demoiselle.
 *
 * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
 * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
 * do Software Livre (FSF).
 *
 * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
 * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
 * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
 * para maiores detalhes.
 *
 * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
 * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
 * ou escreva para a Fundação do Software Livre (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */
package br.gov.frameworkdemoiselle.certificate.signer.pkcs7.bc.attribute;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.esf.OtherHashAlgAndValue;
import org.bouncycastle.asn1.esf.SigPolicyQualifierInfo;
import org.bouncycastle.asn1.esf.SigPolicyQualifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.attribute.SigPolicyQualifierInfoURL;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.attribute.SignaturePolicyId;
import br.gov.frameworkdemoiselle.certificate.signer.pkcs7.attribute.SignaturePolicyIdentifier;

public class BCSignaturePolicyIdentifier extends BCSignedAttribute {

    public BCSignaturePolicyIdentifier(SignaturePolicyIdentifier signaturePolicyIdentifier) {
        super(signaturePolicyIdentifier);
    }

    /**
     * TODO: Implementar a conversão do tipo SigPolicyQualifierInfoUserNotice
     * para BC.
     *
     * @return
     */
    @Override
    public ASN1Set getValue() {
        if (super.getAttribute() == null) {
            org.bouncycastle.asn1.esf.SignaturePolicyIdentifier signaturePolicyIdentifier = new org.bouncycastle.asn1.esf.SignaturePolicyIdentifier();
            return new DERSet(signaturePolicyIdentifier);
        }
        SignaturePolicyId signaturePolicyId = ((SignaturePolicyIdentifier) super.getAttribute()).getSignaturePolicyId();
        if (signaturePolicyId != null) {
            DERObjectIdentifier objectIdentifier = new DERObjectIdentifier(signaturePolicyId.getSigPolicyId());
            OtherHashAlgAndValue otherHashAlgAndValue = new OtherHashAlgAndValue(new AlgorithmIdentifier(signaturePolicyId.getHashAlgorithm()), new DEROctetString(signaturePolicyId.getHash()));
            SigPolicyQualifiers sigPolicyQualifiers = null;
            if (signaturePolicyId.getSigPolicyQualifiers() != null && signaturePolicyId.getSigPolicyQualifiers().size() > 0) {
                List<SigPolicyQualifierInfo> sigPolicyQualifierInfos = new ArrayList<SigPolicyQualifierInfo>();
                for (br.gov.frameworkdemoiselle.certificate.signer.pkcs7.attribute.SigPolicyQualifierInfo sigPolicyQualifierInfo : signaturePolicyId.getSigPolicyQualifiers()) {
                    if (sigPolicyQualifierInfo instanceof SigPolicyQualifierInfoURL) {
                        SigPolicyQualifierInfoURL sigPolicyQualifierInfoURL = (SigPolicyQualifierInfoURL) sigPolicyQualifierInfo;
                        DERObjectIdentifier oi = new DERObjectIdentifier(sigPolicyQualifierInfoURL.getOID());
                        DERIA5String url = new DERIA5String(sigPolicyQualifierInfoURL.getValue());
                        SigPolicyQualifierInfo bcSigPolicyQualifierInfo = new SigPolicyQualifierInfo(oi, url);
                        sigPolicyQualifierInfos.add(bcSigPolicyQualifierInfo);
                    }
                }
                sigPolicyQualifiers = new SigPolicyQualifiers(sigPolicyQualifierInfos.toArray(new SigPolicyQualifierInfo[]{}));
            }
            org.bouncycastle.asn1.esf.SignaturePolicyId bcSignaturePolicyId = new org.bouncycastle.asn1.esf.SignaturePolicyId(objectIdentifier, otherHashAlgAndValue, sigPolicyQualifiers);
            org.bouncycastle.asn1.esf.SignaturePolicyIdentifier signaturePolicyIdentifier = new org.bouncycastle.asn1.esf.SignaturePolicyIdentifier(bcSignaturePolicyId);
            return new DERSet(signaturePolicyIdentifier);
        }
        return new DERSet(new DERNull());
    }

}
