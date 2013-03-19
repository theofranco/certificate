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

package br.gov.frameworkdemoiselle.certificate.extension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;

import br.gov.frameworkdemoiselle.certificate.util.Base64Utils;

/**
 * Provide utilities for Certificate Revocation List, by ICP-BRASIL Patterns
 * 
 * @author CETEC/CTCTA
 */
public class ICPBR_CRL {

	private X509CRL crl = null;

	/**
	 * 
	 * @param is
	 *            -> InputStream
	 * @throws CRLException
	 * @throws CertificateException
	 */
	public ICPBR_CRL(InputStream is) throws CRLException, CertificateException {
		this.crl = getInstance(is);
	}

	/**
	 * 
	 * @param data
	 *            -> byte array
	 * @throws CRLException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public ICPBR_CRL(byte[] data) throws CRLException, CertificateException, IOException {
		this.crl = getInstance(data);
	}

	/**
	 * 
	 * @param data
	 *            -> byte array
	 * @return Object X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws IOException
	 * @throws CertificateException
	 */
	private X509CRL getInstance(byte[] data) throws CRLException, IOException, CertificateException {
		X509CRL crl = null;

		try {
			// Tenta carregar a CRL como se fosse um arquivo binario!
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		} catch (CRLException e) {
			// Nao conseguiu carregar o arquivo. Verifica se ele esta codificado
			// em Base64
			byte[] data2 = null;
			try {
				data2 = Base64Utils.base64Decode(new String(data));
			} catch (Exception e2) {
				// Nao foi possivel decodificar o arquivo em Base64
				throw e;
			}

			ByteArrayInputStream bis = new ByteArrayInputStream(data2);
			crl = getInstance(bis);
			bis.close();
			bis = null;
		}

		return crl;
	}

	/**
	 * 
	 * @param is
	 *            -> InputStream
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 * @throws CRLException
	 * @throws CertificateException
	 * @throws CertificateException
	 */
	private X509CRL getInstance(InputStream is) throws CRLException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X509");
		X509CRL crl = (X509CRL) cf.generateCRL(is);
		return crl;
	}

	/**
	 * returns the CRL
	 * 
	 * @return Objeto X509CRL
	 * @see java.security.cert.X509CRL
	 */
	public X509CRL getCRL() {
		return crl;
	}

}